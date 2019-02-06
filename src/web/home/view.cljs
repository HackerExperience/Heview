(ns web.home.view
  (:require [web.home.login.view :as login.view]))

(defn view []
  [:div#home
   [:div#home-panel-left]
   [:div#home-panel-right]
   [:div.login-section
    [login.view/view]]])
