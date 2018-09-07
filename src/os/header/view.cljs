(ns os.header.view
  (:require [re-frame.core :as rf]))

(defn view
  []
  (let [ip @(rf/subscribe [:game|server|get-ip])]
    [:div "Header"
     [:br]
     [:div
      [:span ip]
      [:br]
      [:button {:on-click #(rf/dispatch [:os|header|inc])} "Inc"]]]))
