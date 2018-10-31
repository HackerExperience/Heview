(ns os.header.view
  (:require [he.core :as he]))

(defn view
  []
  (let [ip (he/subscribe [:game|server|get-ip])]
    [:div "Header"
     [:br]
     [:div
      [:span ip]]]))
