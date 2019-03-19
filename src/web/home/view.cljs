(ns web.home.view
  (:require [he.core :as he]
            [web.home.login.view :as login.view]))

(defn on-sign-up []
  (he/dispatch [:web|home|signup|signup]))

(defn view []
  [:div#home
   [:div#home-panel-left]
   [:div#home-panel-right]
   [:div.login-section
    [login.view/view]]
   [:button
    {:on-click on-sign-up}
    "Sign Up"]])
