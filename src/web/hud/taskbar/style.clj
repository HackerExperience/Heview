(ns web.hud.taskbar.style
  (:require [web.ui.vars :as ui]))

(defn style []
  [[:#hud-taskbar
    {:position :absolute
     :left "145px"
     :bottom "30px"
     :z-index 9999
     :display :flex
     :flex-direction :row
     :max-width "calc(100% - 400px)"
     :overflow-x :hidden}]
   [:.hud-tb-entry
    {:height "50px"
     :border (str "1px solid " ui/color-primary-dark)
     :background-color (ui/color-primary-darkest-rgba "0.5")
     :color ui/color-primary
     :display :flex
     :flex-direction :row
     :align-items :center
     :justify-content :center}
    [:&:hover
     {:cursor :pointer
      :color ui/color-primary-light
      :border (str "1px solid " ui/color-primary)
      :background-color (ui/color-primary-darkest-rgba "0.7")}
     [:>span
      {:color ui/color-primary-light}]]
    [:>i
     {:font-size "30px"}]]
   [".hud-tb-entry + .hud-tb-entry"
    {:margin-left "10px"}]
   [:.hud-tb-full-entry
    {:width "205px"
     :padding "4px 4px 4px 8px"}
    [:>i
     {:flex "0 0"}]
    [:>span
     {:flex "1 1"
      :color (ui/color-primary-light-rgba "0.80")
      :font-size "16px"
      :white-space :nowrap
      :text-overflow :ellipsis
      :overflow-x :hidden}]]
   [:.hud-tb-full-entry-separator
    {:height "85%"
     :border-left (str "1px solid " ui/color-primary-dark)
     :margin "0 5px"}]
   [:.hud-tb-small-entry
    {:min-width "50px"}]])
