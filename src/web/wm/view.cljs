(ns web.wm.view
  (:require [reagent.core :as reagent]
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

(defn data-attributes
  [app-id]
  (let [moving? (he/subscribe [:web|wm|window|moving? app-id])
        focused? (he/subscribe [:web|wm|window|focused? app-id])
        app-type (get-app-type app-id)]
    {:data-app-id app-id
     :data-app-type app-type
     :data-app-moving moving?
     :data-app-focused focused?}))

(defn header-context-switch
  [app-id]
  (let [context (he/subscribe [:web|apps|context app-id])]
    [:button
     {:on-click #(he/dispatch [:web|wm|app|switch-context app-id])}
     (str context " (switch)")]))

(defn action-minimize
  [app-id]
  [:div.app-header-action.app-header-action-minimize
   {:on-click #(he/dispatch [:web|wm|app|minimize app-id])}])

(defn action-close
  [app-id]
  [:div.app-header-action.app-header-action-close
   {:on-click #(he/dispatch [:web|wm|app|close app-id])}])

(defn header-icon []
  [:div.app-header-icon
   [:i.fa.fa-archive]])

(defn header-title []
  [:div.app-header-title "Window title"])

(defn header-actions
  [app-id]
  [:div.app-header-actions
   {:on-mouse-down #(.stopPropagation %)}
   [action-minimize app-id]
   [action-close app-id]])

(defn header-on-mouse-down
  [app-id event]
  (if (= 0 (.-button event))
    (he/dispatch [:web|wm|app|on-header-click
                  app-id
                  {:x (.-pageX event)
                   :y (.-pageY event)}])))

(defn header-on-mouse-up
  [app-id event]
  (if (= 0 (.-button event))
    (he/dispatch [:web|wm|app|on-header-unclick
                  app-id])))

(defn add-header-events
  [app-id]
  (let [moving? (he/subscribe [:web|wm|window|moving? app-id])]
    {:on-mouse-down (partial header-on-mouse-down app-id)
     :on-mouse-up (partial header-on-mouse-up app-id)}))

(defn render-app-header
  [app-id]
  [:div.app-header
   (add-header-events app-id)
   [header-icon]
   [header-title]
   ;; [header-context-switch app-id]
   [header-actions app-id]])

(defn render-app-body
  [app-id]
  [:div.app-body
   [apps.view/view app-id]])

(defn render-app
  [app-id]
  (let [window-data (he/subscribe [:web|wm|window-data app-id])]
    [:div.app
     (merge
      (inline-style window-data)
      (data-attributes app-id))
     [:div.app-container
      {:on-mouse-down #(he/dispatch [:web|wm|window|focus app-id])}
      [render-app-header app-id]
      [render-app-body app-id]]]))

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

(defn render-session
  [sid]
  (let [apps (he/subscribe [:web|wm|session|apps sid])]
    [:div#wm
     [window-tracker]
     (for [app-id apps]
       ^{:key app-id} [render-app app-id])]))

(defn view
  []
  (let [session-id (he/subscribe [:web|wm|active-session])]
    [render-session session-id]))
