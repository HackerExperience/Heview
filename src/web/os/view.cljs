(ns web.os.view
  (:require [reagent.core :as reagent]
            [he.core :as he]
            [web.wm.view :as wm.view]
            [web.hemacs.view :as hemacs.view]
            [web.hud.view :as hud.view]
            [web.os.popups.view]))

(defn context-menu-handler
  [event]
  (.preventDefault event)
  (.stopPropagation event))

(defn on-resize-fn
  [event]
  (he/dispatch [:web|wm|viewport-resized]))
(defonce named-on-resize-fn
  #(on-resize-fn %))

(defn on-os-runtime-error
  [event]
  ;; TODO: Show `web.wm.validators` error once (and only once)
  (when-not (.test #"web.wm.validators:" (.-detail event))
    (he/dispatch [:web|os|error|runtime (.-detail event)])))
(defonce named-on-os-runtime-error
  #(on-os-runtime-error %))

(defn on-javascript-runtime-error
  [event]
  (he.error/runtime (.-message event) :js))
(defonce named-on-javascript-runtime-error
  #(on-javascript-runtime-error %))

(defn on-keydown-fn
  [event]
  (when-not (= (.-key event) "Shift")
    (he/dispatch [:web|hemacs|process-input (.-key event)])
    (if (= (.-key event) "Tab")
      (when-not (= "text" (.-type (.-target event)))
        (.preventDefault event)))))
(defonce named-on-keydown-fn
  #(on-keydown-fn %))

(defn on-focus-in-fn
  [event]
  (he/dispatch [:web|hemacs|input-focused-in]))
(defonce named-on-focus-in-fn
  #(on-focus-in-fn %))

(defn on-focus-out-fn
  [event]
  (he/dispatch [:web|hemacs|input-focused-out]))
(defonce named-on-focus-out-fn
  #(on-focus-out-fn %))

(defn os-event-tracker []
  (.addEventListener js/window "resize" named-on-resize-fn)
  (.addEventListener js/document "os-runtime-error" named-on-os-runtime-error)
  (.addEventListener js/window "error" named-on-javascript-runtime-error)
  (.addEventListener js/document "keydown" named-on-keydown-fn)
  (.addEventListener js/document "focusin" named-on-focus-in-fn)
  (.addEventListener js/document "focusout" named-on-focus-out-fn))

(defn view
  []
  [:div#os
   {:on-context-menu context-menu-handler} ;; TODO
   (os-event-tracker)
   [hemacs.view/view]
   [hud.view/view]
   [wm.view/view]])
