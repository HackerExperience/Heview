(ns web.hud.launcher.style
  (:require [web.ui.vars :as ui]))

(defn style []
  [[:.hud-la-overlay
    {:position :absolute
     :height "100%"
     :width "100%"
     :background-color "rgba(0,0,0,0.75)"
     :z-index 9999}]
   [:.hud-la-overlay-area
    {:display :flex
     :flex-direction :column
     :position :absolute
     :top "200px"
     :left "calc(50% - 386px)"
     :width "772px"
     :height "55%"
     :align-items :center}]
   [:.hud-la-overlay-input-area
    [:>input
     {:height "35px"
      :width "200px"
      :font-size "16px"}]]
   [:.hud-la-overlay-grid
    {:flex "1 1"
     :overflow-y :auto
     :overflow-x :hidden
     :margin-top "20px"
     :width "100%"
     :display :grid
     :grid-gap "5px"
     :grid-template-columns "repeat(auto-fill, 150px)"
     :grid-template-rows "repeat(auto-fill, 100px)"}]
   [:.hud-la-overlay-grid-entry
    {:display :flex
     :flex-direction :column
     :align-items :center
     :justify-content :center
     :background-color (ui/color-primary-darkest-rgba "0.7")
     :color ui/color-primary-light
     :border (str "1px solid " ui/color-primary-dark)}
    [:&:hover
     {:color ui/color-primary-lightest
      :background-color (ui/color-primary-darkest-rgba "0.90")
      :border (str "1px solid " ui/color-primary-light)
      :cursor :pointer}]
    [:>i
     {:font-size "50px"}]
    [:>span
     {:font-size "15px"
      :margin-top "5px"}]]
   [:.hud-la-button
    {:position :absolute
     :left "30px"
     :bottom "30px"
     :border (str "1px solid " ui/color-primary)
     :background-color (ui/color-primary-dark-rgba "0.25")
     :box-shadow (str "0 0 20px 2px " (ui/color-primary-rgba "0.2"))
     :color ui/color-primary
     :padding "5px"
     :z-index 9999
     :font-size "85px"}
    [:&:hover
     {:color ui/color-primary-light
      :cursor :pointer
      :border (str "1px solid " ui/color-primary-light)}]]])
