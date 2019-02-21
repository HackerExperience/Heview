(ns web.os.view
  (:require [he.core :as he]
            [web.wm.view :as wm.view]
            [web.hud.view :as hud.view]
            [web.os.popups.view]))

(defn context-menu-handler
  [event]
  (.preventDefault event)
  (.stopPropagation event))

(defn on-resize-fn []
  (he/dispatch [:web|wm|viewport-resized]))

(defn on-os-runtime-error
  [event]
  (he/dispatch [:web|os|error|runtime (.-detail event)]))

(defn on-javascript-runtime-error
  [event]
  (he.error/runtime (.-message event) :js))

(defn os-event-tracker []
  (.addEventListener js/window "resize" on-resize-fn)
  (.addEventListener js/document "os-runtime-error" on-os-runtime-error)
  (.addEventListener js/window "error" on-javascript-runtime-error))

(defn view
  []
  [:div#os
   {:on-context-menu context-menu-handler} ;; TODO
   (os-event-tracker)
   [hud.view/view]
   [wm.view/view]])
