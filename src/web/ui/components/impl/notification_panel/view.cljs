(ns web.ui.components.impl.notification-panel.view
  (:require [clojure.string :as str]
            [reagent.core :as r]
            [he.core :as he]
            [he.date]))

(defn fetch!
  [coll id]
  (get coll id nil)) ;; TODO

;; Getters

(defn np-class
  [[prefixes _] suffix]
  (let [classes (map #(str %1 "-np-" (name suffix)) prefixes)]
    (str/join " " classes)))

(defn np-callback
  [[_ callbacks] identifier]
  (fetch! callbacks identifier))

;; Model

(defn get-callbacks
  [{on-click :on-click
    on-unread :on-unread
    on-close :on-close}]
  {:click on-click
   :unread on-unread
   :close on-close})

(defn get-classes
  [{raw-prefix :class-prefix}]
  (let [prefix (if (keyword? raw-prefix) (name raw-prefix) raw-prefix)
        custom-prefix (if-not (nil? prefix) prefix [])]
    (into []
          (flatten (conj ["ui-c"] custom-prefix)))))

(defn render-notification-entry
  [meta [entry-id entry] {now :now}]
  (let [client-meta (:client-meta entry)
        icon (:icon client-meta)
        action (:action client-meta)
        targets (:targets client-meta)
        time-ago (he.date/to-time-ago now (:creation-time entry))
        on-click-fn (np-callback meta :click)
        base-root-class (np-class meta :entry)
        root-classes (if-not (:is-read? entry)
                       (str (np-class meta :entry-unread) " " base-root-class)
                       base-root-class)]
    [:div
     {:class root-classes
      :on-click #(on-click-fn entry-id %)}
     [:div {:class (np-class meta :entry-icon)}
      [:i {:class icon}]]
     [:div {:class (np-class meta :entry-body)}
      [:div {:class (np-class meta :entry-body-text-action)} action]
      (for [{text :text icon :icon} targets]
        (let [key (int (* 100000 (.random js/Math)))]
          ^{:key key} [:div {:class (np-class meta :entry-body-text-target)}
                       [:i {:class icon}]
                       [:span text]]))
      [:div {:class (np-class meta :entry-body-footer)} time-ago]]]))

(defn render-notification-empty
  [meta]
  [:div {:class (np-class meta :entries-empty)}
   "There are no notifications :("])

(defn render-notification-entries
  [meta entries]
  (let [now (.now js/Date)
        xargs {:now now}]
    [:div {:class (np-class meta :entries)}
     (if-not (empty? entries)
       (for [[entry-id entry] entries]
         ^{:key entry-id}
         [render-notification-entry meta [entry-id entry] xargs])
       [render-notification-empty meta])]))

(defn render-panel-header
  [meta header-str]
  [:div {:class (np-class meta :header)}
   [:span {:class (np-class meta :header-text)} header-str]])

(defn track-panel-click
  [meta panel-id event]
  (let [target-classes (array-seq (-> event .-target .-classList))
        panel-classes (map #(str % "-np") (first meta))
        class-comp (for [panel-class panel-classes]
                     (some #(str/includes? % panel-class) target-classes))
        outside-click? (not (some true? class-comp))]
    (when outside-click?
      ((np-callback meta :close)))))

(defn add-tracker-event-listeners
  "Notice that we listen twice - for `mousedown` and `click`. As such, `click`
  may seem as overkill or unnecessary, right? Wrong. It's used by Hemacs. It's
  not easy to emulate a mousedown event, so we emulate with onClick. The
  `mousedown`is necessary for mouse users because otherwise it would lead to a
  bad UX when dragging windows (The window would drag on mouse down, but the
  panel would remaing open until mouse up). So, in short, we need (and use) both
  events."
  [click-fn]
  (.addEventListener js/document "mousedown" click-fn)
  (.addEventListener js/document "click" click-fn))

(defn del-tracker-event-listeners
  [click-fn]
  (.removeEventListener js/document "mousedown" click-fn)
  (.removeEventListener js/document "click" click-fn))

(defn render-panel-tracker
  [meta entries panel-id]
  (let [click-fn (partial track-panel-click meta panel-id)
        has-unread? (if-not (empty? entries)
                      (not (:is-read? (last (first entries))))
                      false)]
    (r/create-class
     {:reagent-render
      (fn []
        [:div.ui-c-np-tracker {:style {:display :none}}])
      :component-did-mount
      (fn [comp]
        (add-tracker-event-listeners click-fn)
        (he/dispatch [:web|hemacs|notification-panel-mounted panel-id]))
      :component-will-unmount
      (fn [comp]
        (del-tracker-event-listeners click-fn)
        (he/dispatch [:web|hemacs|notification-panel-unmounted panel-id])
        (when has-unread?
          ((np-callback meta :unread))))})))

(defn notification-panel
  [opts]
  (let [position-class (fetch! opts :position-class)
        entries (fetch! opts :entries)
        class-prefix (get-classes opts)
        header-str (fetch! opts :header-str)
        callbacks (get-callbacks opts)
        meta [class-prefix callbacks]
        root-classes (str position-class " " (np-class meta :container))
        panel-id (str "ui-c-np-id-" (int (* 10000 (.random js/Math))))]
    [:div
     {:id panel-id
      :class root-classes}
     [render-panel-tracker meta entries panel-id]
     [render-panel-header meta header-str]
     [render-notification-entries meta entries]]))
