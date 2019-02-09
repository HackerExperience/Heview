(ns web.os.popups.os-error.view
  (:require [he.core :as he]))

(defn header-on-mouse-move
  [app-id event]
  (let [cloned-node (.cloneNode (.getElementById js/document app-id) true)
        wrapper-div (.createElement js/document "div")
        parent-z-index (.-zIndex (.-style cloned-node))]
    (.appendChild (.getElementById js/document "wm") wrapper-div)
    (.appendChild wrapper-div cloned-node)
    (.add (.-classList wrapper-div) "full-app-entrypoint")
    (set! (.-zIndex (.-style cloned-node)) (- parent-z-index 1))))

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
    (he/dispatch [:web|wm|app|on-header-unclick app-id])))

(defn add-header-events
  [app-id]
  {:on-mouse-move (partial header-on-mouse-move app-id)
   :on-mouse-down (partial header-on-mouse-down app-id)
   :on-mouse-up (partial header-on-mouse-up app-id)})

(defn close-app
  "Close all cloned error windows. Each cloned window will have the same ID,
  hence the loop."
  [app-id]

  (he/dispatch [:web|wm|app|close app-id])

  (js/setTimeout
   #(loop []
      (let [element (.getElementById js/document app-id)]
        (when-not (nil? element)
          (set! (.-outerHTML (.-parentNode element)) "")
          (recur))))
   50))

(defn render-header
  [app-id]
  (let [source (he/subscribe [:web|os|popups|os-error|source app-id])
        source-nice-name (if (= source :js)
                           "Javascript"
                           "Internal")
        title (str "he.exe - " source-nice-name " Runtime error")]
    [:div.os-err-header
     (add-header-events app-id)
     [:div.os-err-title title]
     [:div.os-err-close
      {:on-click #(close-app app-id)}]]))

(defn render-body
  [app-id]
  (let [message (he/subscribe [:web|os|popups|os-error|reason app-id])]
    [:div.os-err-body
     [:div.os-err-icon]
     [:div.os-err-message message]]))

(defn render-footer
  [app-id]
  [:div.os-err-footer
   [:button
    {:on-click #(close-app app-id)}
    "OK"]])

(defn ^:export full-view
  [app-id server-cid]
  [:div.os-err-container
   [render-header app-id]
   [render-body app-id]
   [render-footer app-id]])