(ns web.os.dock.view
  (:require [he.core :as he]))

(defn open-app
  [app-type]
  [:button
   {:on-click #(he/dispatch [:web|wm|app|open app-type])}
   (str "Open " app-type)])

(defn view-launcher []
  [:div.launcher
   [open-app :log-viewer]
   [open-app :explorer]])

(defn view-panel-left []
  [:div#os-dock-panel-left])

(defn view-panel-right []
  [:div#os-dock-panel-right])

(defn view
  []
  [:div#os-dock
   [view-panel-left]
   [view-launcher]
   [view-panel-right]])
