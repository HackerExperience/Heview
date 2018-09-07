(ns os.view
  (:require [os.header.view]))

(defn view
  []
  [:div.os
   (os.header.view/view)
   [:br]
   [:br]
   [:br]
   [:br]
   [:div "Launcher"]])
