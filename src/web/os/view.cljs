(ns web.os.view
  (:require [web.wm.view :as wm.view]
            [web.os.header.view :as header.view]))

(defn view
  []
  [:div.os
   (header.view/view)
   [:br]
   [:br]
   [wm.view/view]
   [:br]
   [:br]
   [:div "Launcher"]])
