(ns web.os.popups.os-error.view
  (:require [cljs.core.match :refer-macros [match]]
            [he.core :as he]))

(defn header-on-mouse-move
  [app-id event]
  (let [cloned-node (.cloneNode (.getElementById js/document app-id) true)
        wrapper-div (.createElement js/document "div")
        parent-z-index (.-zIndex (.-style cloned-node))]
    (.appendChild (.getElementById js/document "wm") wrapper-div)
    (.appendChild wrapper-div cloned-node)
    (.add (.-classList wrapper-div) "wm-app-full-entrypoint")
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
  [app-id message]

  (he/dispatch [:web|wm|app|close app-id])
  (he/dispatch [:web|os|error|runtime-close message])

  (js/setTimeout
   #(loop []
      (let [element (.getElementById js/document app-id)]
        (when-not (nil? element)
          (set! (.-outerHTML (.-parentNode element)) "")
          (recur))))
   50))

(defn render-header
  [app-id reason]
  (let [source (he/subscribe [:web|os|popups|os-error|source app-id])
        source-nice-name (match source
                                :js "Javascript Runtime"
                                :truss "Assertion"
                                :match "Match"
                                :not-implemented "Lazy Developer"
                                _else "Internal")
        title (str "he.exe - " source-nice-name " Error")]
    [:div.a-os-err-header
     (add-header-events app-id)
     [:div.a-os-err-h-title title]
     [:div.a-os-err-h-close
      {:on-click #(close-app app-id reason)}]]))

(defn render-body
  [app-id reason]
  [:div.a-os-err-body
   [:div.a-os-err-b-icon]
   [:div.a-os-err-b-message reason]])

(defn render-footer
  [app-id reason]
  [:div.a-os-err-footer
   [:button
    {:on-click #(close-app app-id reason)}
    "OK"]])

(defn ^:export full-view
  [app-id server-cid]
  (let [reason (he/subscribe [:web|os|popups|os-error|reason app-id])]
    [:div.a-os-err-container
     [render-header app-id reason]
     [render-body app-id reason]
     [render-footer app-id reason]]))
