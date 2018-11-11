(ns web.os.dock.view
  (:require [he.core :as he]))

(defn launch-app
  [app-type]
  [:button
   {:on-click #(he/dispatch [:web|wm|app|launch app-type])}
   (str "Launch " app-type)])

(defn view
  []
  [:div "Launcher"
   [:br]
   [launch-app :log-viewer]])
