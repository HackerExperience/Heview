(ns web.hud.system-tray.view
  (:require [he.core :as he]))

(defn render-daemon-area []
  [:div.hud-st-daemon-area
   [:div.hud-st-daemon-entry
    [:i.fab.fa-freebsd]]
   [:div.hud-st-daemon-entry
    [:i.fab.fa-dev]]
   [:div.hud-st-daemon-entry
    [:i.fas.fa-wifi]]])

(defn render-clock []
  [:div.hud-st-clock-area
   [:span.hud-st-clock-day "26/01"]
   [:span.hud-st-clock-time "12:21"]])

(defn view []
  [:div#hud-system-tray
   [render-daemon-area]
   [:div.hud-st-clock-separator]
   [render-clock]])
