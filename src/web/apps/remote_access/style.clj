(ns web.apps.remote-access.style
  (:require [web.ui.vars :as ui]))

(defn browse []
  [:.ra-browse
   {:display :flex
    :flex-direction :row
    :height "100%"
    :align-items :center
    :justify-content :center
    :padding "5px"}
   [:.ra-browse-input
    {:flex "1 1"
     :align-items :center
     :min-width "125px"
     :max-width "125px"}
    [:input
     {:height "26px"
      :width "120px"}]]
   [:.ra-browse-button
    [:button
     {:height "26px"}]
    [:.ra-browse-button-spinner
     {:width "30px"
      :height "26px"
      :border (str "1px solid " ui/color-primary-light)
      :display :flex
      :align-items :center
      :justify-content :center}
     [:i
      {:font-size "16px"}]]]])

(defn auth []
  [:.ra-auth
   {:display :flex
    :flex-direction :column
    :height "100%"}
   [:.ra-auth-login-area
    {:flex "1 1"
     :display :flex
     :flex-direction :column
     :align-items :center
     :justify-content :center}
    [:.ra-auth-login-username
     [:input
      {:cursor :default}]]
    [:.ra-auth-login-password
     {:margin-top "10px"}]
    ]
   [:.ra-auth-action-area
    {:flex "0 0"
     :border-top (str "1px solid " ui/color-primary-darker)
     :background-color (ui/color-primary-darkest-rgba "0.6")
     :min-height "46px"
     :display :flex
     :flex-direction :row
     :align-items :center}
    [:.ra-auth-action-bruteforce-area
     {:margin-left "15px"}]
    [:.ra-auth-action-login-area
     {:margin-left :auto
      :margin-right "15px"}]]])

(defn remote []
  [:.ra-remote
   {}])

(defn local-style []
  [[:.ra-container
    {:height "100%"}
    [[(browse)]
     [(auth)]
     [(remote)]]]])

(defn global-style []
  [])
