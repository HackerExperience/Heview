(ns web.apps.remote-access.style
  (:require [web.ui.vars :as ui]))

(defn browse []
  [[:.a-ra-browse
    {:display :flex
     :flex-direction :row
     :height "100%"
     :align-items :center
     :justify-content :center
     :padding "5px"}]
   [:.a-ra-br-input
    {:flex "1 1"
     :align-items :center
     :min-width "125px"
     :max-width "125px"}
    [:>input
     {:height "26px"
      :width "120px"}]]
   [:.a-ra-br-button
    [:>button
     {:height "26px"}]]
   [:.a-ra-br-button-spinner
    {:width "30px"
     :height "26px"
     :border (str "1px solid " ui/color-primary-light)
     :display :flex
     :align-items :center
     :justify-content :center
     :font-size "16px"}]])

(defn auth []
  [[:.a-ra-auth
    {:display :flex
     :flex-direction :column
     :height "100%"}]
   [:.a-ra-au-login-area
    {:flex "1 1"
     :display :flex
     :flex-direction :column
     :align-items :center
     :justify-content :center}]
   [:.a-ra-au-login-username
    [:>input
     {:cursor :default}]]
   [:.a-ra-au-login-password
    {:margin-top "10px"}]
   [:.a-ra-au-action-area
    {:flex "0 0"
     :border-top (str "1px solid " ui/color-primary-darker)
     :background-color (ui/color-primary-darkest-rgba "0.6")
     :min-height "46px"
     :display :flex
     :flex-direction :row
     :align-items :center}]
   [:.a-ra-au-action-bruteforce-area
    {:margin-left "15px"}]
   [:.a-ra-au-action-login-area
    {:margin-left :auto
     :margin-right "15px"}]])

(defn remote []
  [:.a-ra-remote
   {}])

(defn style []
  [[:.a-ra-container
    {:height "100%"}]
   [(browse)]
   [(auth)]
   [(remote)]])
