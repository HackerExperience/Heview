(ns web.home.view
  (:require [web.home.login.view :as login.view]))

(defn view []
  [:div "Homee"
   [:br]
   (login.view/view)])
