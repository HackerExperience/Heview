(ns web.hud.connection-info.style
  (:require [web.ui.vars :as ui]))

(defn notification-panel []
  [[:.hud-ci-np-container
    {:top "75px"}]
   [:.hud-ci-np-gateway
    {:left "-125px"}]
   [:.hud-ci-np-endpoint
    {:right "-125px"}]])

(defn server-selector []
  [[:.hud-ci-server-selector-gateway
    {:position :absolute
     :left "-40px"
     :top "75px"}]
   [:.hud-ci-server-selector-dd-container
    {:min-width "250px"
     :min-height "0px"
     :border :none}]
   [:.hud-ci-server-selector-dd-selected-container
    {:display :none}]
   [:.hud-ci-server-selector-dd-drop
    {:top "0"
     :border-top (str "1px solid " ui/color-primary-light)}]
   [:.hud-ci-server-selector-dd-drop-group-name
    {:justify-content :center}]
   [:.hud-ci-server-selector-dd-drop-entry
    [:&:hover
     {:border "1px solid transparent"}]]
   [".hud-ci-server-selector-entry + .hud-ci-server-selector-entry"
    {:margin-top "5px"}]
   [:.hud-ci-server-selector-dd-drop-group
    [:>.hud-ci-server-selector-dd-drop-entry
     {:padding-left "0px"}]]
   [:.hud-ci-server-selector-entry
    {:min-width "100%"
     :color ui/color-primary-light
     :border (str "1px solid " ui/color-primary)
     :display :flex
     :flex-direction :row
     :align-items :center
     :padding-left "15px"
     :min-height "40px"}
    [:&:hover
     {:border (str "1px solid " ui/color-primary-light)}]]
   [:.hud-ci-server-selector-entry-icon
    {:font-size "26px"
     :color (ui/color-primary-light-rgba "0.7")}]
   [:.hud-ci-server-selector-entry-body
    {:display :flex
     :flex-direction :column
     :padding-left "10px"
     :font-family ui/font-monospace}]
   [:.hud-ci-server-selector-entry-body-name
    {:padding-bottom "2px"}]
   [:.hud-ci-server-selector-entry-body-hardware
    {:font-size "9px"
     :color ui/color-secondary-light}]
   [:.hud-ci-server-selector-entry-body-hardware-dot
    {:height "3px"
     :width "3px"
     :display :inline-block
     :border (str "1px solid " ui/color-secondary)
     :background-color ui/color-secondary-dark
     :margin "0 3px 1px 3px"}]])

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
   [:.hud-ci-server
    {:flex "1 1"
     :display :flex
     :flex-direction :column
     :align-items :center
     :min-height "100%"
     :padding "8px 0 7px 0"
     :margin "-8px 0 -7px 0"
     :position :relative}]
    [:.hud-ci-server-icon
     {:font-size "36px"
      :color (ui/color-primary-rgba "1.0")
      :text-shadow "0 0 #000"
      :min-width "100%"
      :display :flex
      :justify-content :center
      :border "1px solid transparent"}
     [:&:hover
      {:background-color ui/color-primary-darker
       :cursor :pointer
       :border (str "1px solid " ui/color-primary-dark)}]]
    [:.hud-ci-server-name
     {:color ui/color-primary-light
      :margin-top "2px"
      :text-shadow "0 0 #000"
      :padding "2px"
      :font-size "12px"
      :min-width "100%"
      :display :flex
      :justify-content :center
      :border "1px solid transparent"}
     [:&:hover
      {:background-color ui/color-primary-darker
       :cursor :pointer
       :border (str "1px solid " ui/color-primary-dark)}
      [:.hud-ci-server-selector
       {:color ui/color-primary-light}]]
     [:>span
      {:position :relative}]]
   [:.hud-ci-server-selector
    {:position :absolute
     :left "-11px"
     :top "2px"
     :color ui/color-primary}]
   [:.hud-ci-server-active-desktop
    [:>.hud-ci-server-icon
     {:color ui/color-primary-lightest}]
    ;; [:>span
    ;;  {:color ui/color-primary-light}]
    ]
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
   [(notification-panel)]
   [(server-selector)]])
