(ns web.home.view
  (:require [web.home.login.view :as login.view]))

(defn view []
  [:div "Home"
   [:br]
   (login.view/view)])
