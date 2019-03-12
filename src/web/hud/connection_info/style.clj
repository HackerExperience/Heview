(ns web.hud.connection-info.style
  (:require [web.ui.vars :as ui]))

(defn notification-panel []
  [[:.hud-ci-np-container
    {:top "75px"}]
   [:.hud-ci-np-gateway
    {:left "-125px"}]
   [:.hud-ci-np-endpoint
    {:right "-125px"}]])

(defn style []
  [[:#hud-connection-info
    {:position :absolute
     :left "calc(50% - 225px)"
     :display :flex
     :justify-content :center
     :height "70px"}]
   [:.hud-ci-area
    {:position :relative
     :display :flex
     :background-color (ui/color-primary-darkest-rgba "0.50")
     :margin-top "15px"
     :border (str "1px solid" ui/color-primary-lightest)
     :box-shadow (str "0 0 10px 5px " (ui/color-primary-darkest-rgba "1.0"))
     :flex-direction :row
     :align-items :center
     :justify-content :center
     :min-height "100%"
     :padding "5px 0"
     :min-width "450px"
     :z-index 9999}]
   [:.hud-ci-side
    {:flex "0 0"
     :display :flex
     :flex-direction :column
     :min-width "35px"
     :align-items :center
     :color ui/color-primary-dark
     :font-size "20px"}
    [:i
     [:&:hover
      {:color ui/color-primary
       :cursor :pointer
       :padding "3px 4px"
       :margin "-3px -4px"}]]]
   [:.hud-ci-side-desktop
    {:flex "1 1"
     :display :flex
     :align-items :center}]
   [:.hud-ci-side-active-desktop
    [:>i
     {:color ui/color-primary-light}
     [:&:hover
      {:color ui/color-primary-light
       :cursor :initial}]]]
   [:.hud-ci-side-bell
    {:flex "1 1"
     :display :flex
     :align-items :center}]
   [:.hud-ci-icon
    {:flex "1 1"
     :display :flex
     :flex-direction :column
     :align-items :center
     :min-height "100%"
     :padding "8px 0 7px 0"
     :margin "-8px 0 -7px 0"
     :position :relative}
    [:&:hover
     {:background-color ui/color-primary-darker
      :cursor :pointer}]
    [:>i
     {:font-size "36px"
      :color (ui/color-primary-rgba "1.0")
      :text-shadow "0 0 #000"}]
    [:>span
     {:color ui/color-primary-light
      :margin-top "2px"
      :text-shadow "0 0 #000"
      :font-size "12px"}]]
   [:.hud-ci-icon-active-desktop
    [:>i
     {:color ui/color-primary-lightest}]
    [:>span
     {:color ui/color-primary-light}]]
   [:.hud-ci-gateway-area
    {:min-width "150px"
     :display :flex
     :flex-direction :row}]
   [:.hud-ci-gateway-icon
    {:flex "1 1"}]
   [:.hud-ci-bounce-area
    {:background "#333"
     :min-width "150px"}]
   [:.hud-ci-endpoint-area
    {:min-width "150px"
     :display :flex
     :flex-direction :row
     :justify-content :center}]
   [:.hud-ci-endpoint-icon
    {:flex "1 1"}]
   [:.hud-ci-endpoint-nil
    {:display :flex
     :flex-direction :column
     :align-items :center}
    [:>i
     {:flex "1 1"
      :color ui/color-primary-dark
      :font-size "36px"}]
    [:>span
     {:flex "1 1"
      :margin-top "2px"
      :color ui/color-primary}]]
   [(notification-panel)]])
