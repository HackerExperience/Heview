(ns web.ui.components.impl.dropdown.view
  (:require [reagent.core :as r]
            [clojure.string :as str]
            [cljs.core.match :refer-macros [match]]
            [he.core :as he]
            [web.hemacs.mode.dropdown :as dropdown-mode]))

;; Drop tracker ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn walk-dd-container
  ([element drop-id]
   (walk-dd-container element drop-id 0))
  ([element drop-id seeks]
   (cond
     (>= seeks 7) nil
     (= (.-id element) drop-id) element
     :else (walk-dd-container (.-parentElement element) drop-id (inc seeks)))))

(defn track-drop-click
  [meta state drop-id event]
  (let [target-classes (array-seq (-> event .-target .-classList))
        drop-classes (map #(str % "-dd") (first meta))
        close-fn #(swap! % assoc
                         :drop-showing? false
                         :search-query ""
                         :current-entries (:original-entries @%)
                         :current-groups (:original-groups @%))
        class-comp (for [drop-class drop-classes]
                     (some #(str/includes? % drop-class) target-classes))
        outside-click? (not (some true? class-comp))]
    (if-not outside-click?
      (let [dd-container (walk-dd-container (.-target event) drop-id)
            dd-container-id (when dd-container
                              (.-id dd-container))]
        (when-not (= drop-id dd-container-id)
          (close-fn state)))
      (close-fn state))))

(defn track-drop-keydown
  [meta state drop-id event]
  (let [fake-args [nil nil nil {:dropdown-id drop-id}]
        fn-esc #(dropdown-mode/input-Escape fake-args)]
    (match (.-key event)
           "ArrowDown" (dropdown-mode/input-j fake-args)
           "ArrowUp" (do
                       (dropdown-mode/input-k fake-args)
                       (.preventDefault event))
           "Enter" (dropdown-mode/input-Enter fake-args)
           "Escape" (dropdown-mode/input-Escape fake-args)
           "PageDown" (dropdown-mode/input-PageDown fake-args)
           "PageUp" (dropdown-mode/input-PageUp fake-args)
           "Tab" (do
                   (dropdown-mode/input-j fake-args)
                   (.preventDefault event))
           _ :noop))

  ;; We are already handling this. Do not let hemacs handle again.
  ;; Since this keydown event is only called if hemacs is disabled, stopping the
  ;; propagation has the sole goal of not letting the user enable hemacs while
  ;; this input is focused.
  (.stopPropagation event))

(defn add-tracker-event-listeners
  [drop-el {click-fn :click keydown-fn :keydown} hemacs-enabled?]
  (.addEventListener js/document "click" click-fn)
  (when-not hemacs-enabled?
    (.addEventListener drop-el "keydown" keydown-fn)))

(defn del-tracker-event-listeners
  [drop-el {click-fn :click keydown-fn :keydown} _]
  (.removeEventListener js/document "click" click-fn)
  (.removeEventListener drop-el "keydown" keydown-fn))

;; Default callbacks ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- default-selected-click
  [state event]
  (swap! state assoc :drop-showing? (not (:drop-showing? @state))))

(defn- default-drop-entry-click
  [external-callback state new-entry-id event]
  (let [old-entry-id (:entry-id @state)]
    (swap! state assoc
           :search-query ""
           :current-entries (:original-entries @state)
           :current-groups (:original-groups @state)
           :drop-showing? false
           :entry-id new-entry-id)
    (when-not (= new-entry-id old-entry-id)
      (external-callback new-entry-id))))

(defn default-search-fn
  [query entries]
  (let [reducer (fn [acc entry]
                  (if (str/includes? (str/lower-case (:label entry))
                                     (str/lower-case query))
                    (conj acc entry)
                    acc))]
    (reduce reducer [] entries)))

;; Default renderers ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn dd-class
  [[prefixes _ _] suffix]
  (let [classes (map #(str %1 "-dd-" (name suffix)) prefixes)]
    (str/join " " classes)))

(defn dd-renderer
  [[_ renderers _] identifier]
  (get renderers identifier))

(defn dd-callback
  [[_ _ callbacks] identifier]
  (get callbacks identifier))

(defn- default-caret-down-renderer []
  [:i.fa.fa-caret-down])

(defn- default-caret-up-renderer []
  [:i.fa.fa-caret-up])

(defn- default-selected-renderer
  [_ entry]
  [:span (:label entry)])

(defn- default-drop-simple-renderer
  [meta entry]
  [:span (:label entry)])

(defn- default-drop-full-renderer
  [meta state entry]
  (let [renderer (dd-renderer meta :drop-simple)
        base-class (dd-class meta :drop-entry)
        callback-drop-click (dd-callback meta :drop-entry-click)
        on-change-fn (dd-callback meta :on-change)
        class (if (= (:entry-id @state) (:id entry))
                (str base-class " " (dd-class meta :drop-entry-selected))
                base-class)]
    [:div
     {:class class
      :on-click #(callback-drop-click on-change-fn state (:id entry) %)}
     [renderer meta entry]]))

;; "Model" ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-renderers
  [{custom-selected-renderer :selected
    custom-drop-full-renderer :drop-full
    custom-drop-simple-renderer :drop-simple
    custom-caret-down-renderer :caret-down
    custom-caret-up-renderer :caret-up}]
  {:caret-down (or custom-caret-down-renderer default-caret-down-renderer)
   :caret-up (or custom-caret-up-renderer default-caret-up-renderer)
   :selected (or custom-selected-renderer default-selected-renderer)
   :drop-simple (or custom-drop-simple-renderer default-drop-simple-renderer)
   :drop-full (or custom-drop-full-renderer default-drop-full-renderer)})

(defn get-callbacks
  [{{custom-selected-click :selected-click
     custom-drop-entry-click :drop-entry-click
     custom-search-fn :search} :callbacks
    on-change :on-change}]
  {:selected-click (or custom-selected-click default-selected-click)
   :drop-entry-click (or custom-drop-entry-click default-drop-entry-click)
   :search (or custom-search-fn default-search-fn)
   :on-change on-change})

(defn get-classes
  [{raw-prefix :class-prefix
    full-view? :full-view?}]
  (let [prefix (if (keyword? raw-prefix) (name raw-prefix) raw-prefix)
        custom-prefix (if-not (nil? prefix) prefix [])
        std-prefix (if full-view?
                     []
                     ["ui-c"])]
    (into []
          (flatten (conj std-prefix custom-prefix)))))

(defn get-dropdown-id
  [opts]
  (get opts :dropdown-id (str "ui-c-dd-" (int (* 10000 (.random js/Math))))))

(defn get-entry
  [entry-cache entry-id]
  (get entry-cache entry-id))

(defn create-entry-cache
  [entries]
  (let [reducer (fn [acc entry]
                  (assoc acc (:id entry) entry))]
    (reduce reducer {} entries)))

(defn group-entries-reducer
  [acc entry]
  (update acc (:group entry) #(conj % entry)))

(defn group-entries
  [entries]
  (reduce group-entries-reducer {} entries))

(defn create-initial-state
  [opts]
  (let [entries (:entries opts)
        grouped? (:grouped? opts)
        group-specific-map (when grouped?
                             (let [grouped-entries (group-entries entries)]
                               {:original-groups grouped-entries
                                :current-groups grouped-entries}))]

    (merge
     {:entry-id (:entry-id opts)
      :drop-showing? (get opts :drop-showing? false)
      :original-entries entries
      :current-entries entries
      :search? (get opts :search? true)
      :search-query ""
      :grouped? grouped?}
     group-specific-map)))

;; Search implementation ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn perform-search-post-group-search
  "Removes groups that have no matching entries"
  [new-group-values]
  (reduce (fn [acc [group-name group-entries]]
            (if-not (empty? group-entries)
              (assoc acc group-name group-entries)
              acc)) {} new-group-values))

(defn perform-search-group-reducer
  [[perform-search-fn meta query original-values] acc [grp-name grp-entries]]
  (let [original-entries (get original-values grp-name)
        searched-values
        (perform-search-fn meta query [original-entries grp-entries] false)]
    (assoc acc grp-name searched-values)))

(defn perform-search
  [meta [old-query new-query] [original-values current-values] grouped?]
  (let [search-fn (dd-callback meta :search)
        searchable-values (if (> (count new-query) (count old-query))
                            current-values
                            original-values)]
    (if-not grouped?
      (search-fn new-query searchable-values)
      (let [params [perform-search meta [old-query new-query] original-values]
            reducer (partial perform-search-group-reducer params)]
        (-> reducer
            (reduce {} searchable-values)
            (perform-search-post-group-search))))))

(defn on-search-input-change
  [meta s event]
  (let [old-query (:search-query @s)
        new-query (-> event .-target .-value)
        grouped? (:grouped? @s)
        [original-values current-values relevant-current-key]
        (if-not grouped?
          [(:original-entries @s) (:current-entries @s) :current-entries]
          [(:original-groups @s) (:current-groups @s) :current-groups])
        new-current-values (perform-search meta
                                           [old-query new-query]
                                           [original-values current-values]
                                           grouped?)]
    (swap! s assoc
           :search-query new-query
           relevant-current-key new-current-values)))

;; Rendereres ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn render-drop-empty
  [meta]
  [:div {:class (dd-class meta :drop-entries-empty)}
   "No results found."])

(defn render-drop-entries
  [meta state renderer entries]
  (if-not (empty? entries)
    (for [entry entries]
      (let [key (str "dd-drop-entry-" (:id entry))]
        ^{:key (:id entry)} [renderer meta state entry]))
    [render-drop-empty meta]))

(defn render-drop-grouped
  [meta state renderer]
  (let [current-groups (:current-groups @state)]
    (if-not (empty? current-groups)
      (for [[group-name group-entries] (:current-groups @state)]
        (let [key (str "dd-drop-grp-" group-name)]
          ^{:key key}
          [:div {:class (dd-class meta :drop-group)}
           [:span {:class (dd-class meta :drop-group-name)}
            group-name]
           (render-drop-entries meta state renderer group-entries)]))
      [render-drop-empty meta])))

(defn render-drop-tracker
  [meta state]
  (let [hemacs-enabled? (he/subscribe [:web|hemacs|enabled?])
        dropdown-id (last meta)
        click-fn (partial track-drop-click meta state dropdown-id)
        keydown-fn (partial track-drop-keydown meta state dropdown-id)
        functions {:click click-fn
                   :keydown keydown-fn}]
    (r/create-class
     {:reagent-render
      (fn []
        [:div.ui-c-dd-drop-tracker {:style {:display :none}}])
      :component-did-mount
      (fn [comp]
        (let [drop-el (.getElementById js/document dropdown-id)]
          (add-tracker-event-listeners drop-el functions hemacs-enabled?)
          (he/dispatch [:web|hemacs|dropdown-mounted dropdown-id])))
      :component-will-unmount
      (fn [comp]
        (let [drop-el (.getElementById js/document dropdown-id)]
          (del-tracker-event-listeners drop-el functions hemacs-enabled?)
          (he/dispatch [:web|hemacs|dropdown-unmounted dropdown-id])))})))

(defn render-drop-search-input
  [meta state]
  (let [hemacs-enabled? (he/subscribe [:web|hemacs|enabled?])]
    (r/create-class
     {:reagent-render
      (fn []
        [:input
         {:class (str (dd-class meta :drop-search-input) " ui-input")
          :value (:search-query @state)
          :on-change #(on-search-input-change meta state %)}])
      :component-did-mount
      (fn [comp]
        (when-not hemacs-enabled?
          (let [element (r/dom-node comp)]
            (.focus element))))})))

(defn render-drop-search-area
  [meta state]
  [:div {:class (dd-class meta :drop-search-container)}
   [render-drop-search-input meta state]
   [:div {:class (dd-class meta :drop-search-icon)}
    [:i.fa.fa-search]]])

(defn render-drop
  [meta state]
  (let [renderer (dd-renderer meta :drop-full)]
    [:div {:class (dd-class meta :drop)}
     [render-drop-tracker meta state]
     (when (:search? @state)
       [render-drop-search-area meta state])
     [:div
      {:class (dd-class meta :drop-entries)
       :style {:max-height "300px"}}
      (if (:grouped? @state)
        (render-drop-grouped meta state renderer)
        (render-drop-entries meta state renderer (:current-entries @state)))]]))

(defn render-selected-caret
  [meta state]
  (let [caret-renderer (if (:drop-showing? @state)
                         (dd-renderer meta :caret-up)
                         (dd-renderer meta :caret-down))]
    [:div
     {:class (dd-class meta :selected-caret-area)}
     (caret-renderer)]))

(defn render-selected
  [meta state entry-cache]
  (let [renderer (dd-renderer meta :selected)
        entry (get-entry entry-cache (:entry-id @state))]
    [:div
     {:class (dd-class meta :selected-container)
      :on-click #((dd-callback meta :selected-click) state %)}
     [:div
      {:class (dd-class meta :selected-entry)}
      [renderer meta entry]]
     [render-selected-caret meta state]]))

(defn dropdown
  [opts]
  (let [cache (create-entry-cache (:entries opts))
        state (r/atom (create-initial-state opts))
        renderers (get-renderers (:renderers opts))
        class-prefix (get-classes opts)
        callbacks (get-callbacks opts)
        dropdown-id (get-dropdown-id opts)
        meta [class-prefix renderers callbacks dropdown-id]]
    (fn []
      (let [drop-showing? (:drop-showing? @state)]
        [:div
         {:id dropdown-id
          :class (dd-class meta :container)
          :data-drop drop-showing?}
         (render-selected meta state cache)
         (when drop-showing?
           (render-drop meta state))]))))
