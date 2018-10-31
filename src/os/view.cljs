(ns os.view
  (:require [wm.view]
            [os.header.view]))

(defn view
  []
  [:div.os
   (os.header.view/view)
   [:br]
   [:br]
   [wm.view/view]
   [:br]
   [:br]
   [:div "Launcher"]])
