(ns web.hud.system-tray.style
  (:require [web.ui.vars :as ui]))

(defn style []
  [[:#hud-system-tray
    {:position :absolute
     :bottom "30px"
     :right "30px"
     :width "130px"
     :height "40px"
     :display :flex
     :flex-direction :row
     :border (str "1px solid " ui/color-primary-dark)
     :background-color (ui/color-primary-darkest-rgba "0.6")
     :z-index 9999
     :align-items :center}]
   [:.hud-st-daemon-area
    {:flex "1 1"
     :padding-left "3px"
     :display :flex
     :flex-direction :row-reverse}]
   [:.hud-st-daemon-entry
    {:color ui/color-primary
     :display :flex
     :flex-direction :row
     :font-size "20px"}
    [:&:hover
     {:color ui/color-primary-light
      :cursor :pointer}]]
   [".hud-st-daemon-entry + .hud-st-daemon-entry"
    {:margin-right "5px"}]
   [:.hud-st-clock-separator
    {:flex "0 0"
     :height "80%"
     :margin "0 5px"
     :border-left (str "1px solid " (ui/color-primary-dark-rgba "0.7"))}]
   [:.hud-st-clock-area
    {:flex "0 0"
     :display :flex
     :flex-direction :column
     :margin-left :auto
     :width "55px"
     :text-align :center
     :color ui/color-primary-light
     :padding-right "3px"
     :font-family :monospace}]])
