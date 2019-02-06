(ns web.os.view
  (:require [he.core :as he]
            [web.wm.view :as wm.view]
            [web.os.header.view :as header.view]
            [web.os.dock.view :as dock.view]))

(defn context-menu-handler
  [event]
  (.preventDefault event)
  (.stopPropagation event))

(defn on-resize-fn []
  (he/dispatch [:web|wm|viewport-resized]))

(defn os-event-tracker []
  (.addEventListener js/window "resize" on-resize-fn))

(defn view
  []
  [:div#os
   {:on-context-menu context-menu-handler} ;; TODO
   (os-event-tracker)
   [header.view/view]
   [wm.view/view]
   [dock.view/view]])
