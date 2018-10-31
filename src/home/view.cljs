(ns home.view
  (:require [home.login.view]))

(defn view []
  [:div "Home"
   [:br]
   (home.login.view/view)])
