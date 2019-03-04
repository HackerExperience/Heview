(ns web.wm.view
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [re-frame.core :as rf]
            [he.core :as he]
            [web.apps.view :as apps.view]))

(defn inline-style
  [window-data]
  (let [{{x :x y :y} :position
         {len-x :x len-y :y} :length
         z-index :z-index} window-data]
    {:style
     {:top (str y "px")
      :left (str x "px")
      :height (str len-y "px")
      :width (str len-x "px")
      :z-index z-index}}))

(defn get-app-type
  [app-id]
  (let [{type :type
         popup :popup} (he/subscribe [:web|apps|meta app-id])]
    (if (= type :popup)
      (let [{app-type :app-type
             popup-type :popup-type} popup]
        (str (name app-type) "-" (name popup-type)))
      (str (name type)))))

(defn app-classes
  [app-id]
  (let [moving? (he/subscribe [:web|wm|window|moving? app-id])
        focused? (he/subscribe [:web|wm|window|focused? app-id])
        app-type (get-app-type app-id)
        moving-class (when moving? "app-moving")
        focused-class (when focused? "app-focused")
        app-type-class (str "app-type-" (name app-type))]
    {:id app-id
     :class (str/join " " [moving-class focused-class app-type-class])}))

(defn app-events
  [app-id]
  {:on-mouse-down #(he/dispatch [:web|wm|window|focus app-id])})

(defn header-context-switch
  [app-id config]
  (let [session (he/subscribe [:web|wm|current-session])
        context-name (he/subscribe [:web|apps|context-nice-name app-id])
        can-switch? (and
                     (not (nil? (:endpoint session)))
                     (get config :contextable true))]
    (when (:show-context config)
      [:div.app-header-context
       (if can-switch?
         {:on-click #(he/dispatch [:web|wm|app|switch-context app-id])
          :tip "Switch context"}
         {:class :app-header-context-disabled})
       [:span context-name]
       [:i.fas.fa-sync-alt]])))

(defn header-seq-id
  [app-id]
  (let [seq-id (he/subscribe [:web|wm|window|seq-id app-id])]
    [:div.app-header-seq-id
     [:span seq-id]]))

(defn header-icon
  [config]
  [:div.app-header-icon
   [:i
    {:class (:icon-class config)}]])

(defn header-title
  [config]
  [:div.app-header-title (:title config)])

(defn header-actions
  [app-id config]
  [:div.app-header-actions
   {:on-click #(.stopPropagation %)}
   (when (:show-minimize config)
     [:div.app-header-action
      {:on-click #(he/dispatch [:web|wm|app|minimize app-id])}
      [:i.far.fa-window-minimize]])
   (when (:show-close config)
     [:div.app-header-action
      {:on-click #(he/dispatch [:web|wm|app|close app-id])}
      [:i.far.fa-window-close]])])

(defn header-on-mouse-down
  [app-id event]
  (if (= 0 (.-button event))
    (he/dispatch [:web|wm|app|on-header-click
                  app-id
                  {:x (.-pageX event)
                   :y (.-pageY event)}])))

(defn header-on-mouse-up
  [app-id event]
  (when (= 0 (.-button event))
    (he/dispatch [:web|wm|app|on-header-unclick app-id])))

(defn add-header-events
  [app-id]
  {:on-mouse-down (partial header-on-mouse-down app-id)
   :on-mouse-up (partial header-on-mouse-up app-id)})

(defn render-app-header
  [app-id]
  (let [config (he/subscribe [:web|wm|window|config app-id])
        hemacs-enabled? (he/subscribe [:web|hemacs|enabled?])]
    [:div.app-header
     (add-header-events app-id)
     (when hemacs-enabled?
       [header-seq-id app-id])
     [header-icon config]
     [:div.app-header-icon-separator]
     [header-title config]
     [header-context-switch app-id config]
     [header-actions app-id config]]))

(defn render-app-body
  [app-id]
  [:div.app-body
   [apps.view/view app-id]])

(defn render-app
  [app-id]
  (let [window-data (he/subscribe [:web|wm|window|data app-id])
        {{full-view :full-view} :config} window-data
        attributes (merge
                    (inline-style window-data)
                    (app-classes app-id)
                    (app-events app-id))]
    (if full-view
      [:div.full-app-entrypoint
       [:div.full-app
        attributes
        [:div.full-app-container
         [apps.view/full-view app-id]]]]
      [:div.app
       attributes
       [:div.app-container
        [render-app-header app-id]
        [render-app-body app-id]]])))

(defn on-move-fn
  [event]
  (he/dispatch [:web|wm|app|on-header-move
                {:x (-> event .-pageX) :y (-> event .-pageY)}]))

(defn on-up-fn
  [_event]
  (he/dispatch [:web|wm|app|on-header-unclick]))

(defn window-tracker-register-listeners []
  (.addEventListener js/window "mousemove" on-move-fn)
  (.addEventListener js/window "mouseup" on-up-fn))

(defn window-tracker-deregister-listeners []
  (.removeEventListener js/window "mousemove" on-move-fn)
  (.removeEventListener js/window "mouseup" on-up-fn))

(defn window-tracker-inner []
  (let [update (fn [comp]
                 (let [{moving-id :moving-id} (reagent/props comp)]
                   (if (nil? moving-id)
                     (window-tracker-deregister-listeners)
                     (window-tracker-register-listeners))))]
    (reagent/create-class
     {:reagent-render (fn [] [:div.window-tracker {:display :none}])
      :component-did-mount (fn [comp] (update comp))
      :component-did-update update})))

(defn window-tracker []
  (let [moving-id (rf/subscribe [:web|wm|has-window-moving?])]
    (fn []
      [window-tracker-inner {:moving-id @moving-id}])))

(defn view []
  (let [apps (he/subscribe [:web|wm|current-session|apps])]
    [:div#wm
     [window-tracker]
     (for [app-id apps]
       ^{:key app-id} [render-app app-id])]))

;; (defn view
;;   []
;;   (let [session-id (he/subscribe [:web|wm|active-session])]
;;     [render-session session-id]))
