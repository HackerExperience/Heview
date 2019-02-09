(ns web.os.popups.confirm.style
  (:require [web.ui.vars :as ui]))

(defn body []
  [:.os-cfm-body
   {:height "60%"
    :background (ui/color-primary-darkest-rgba "0.2")}
   [:.os-cfm-message-area
    {:height "100%"
     :display :flex
     :flex-direction :row
     :align-items :center}
    [:.os-cfm-message-icon-area
     {:flex "0 0"
      :margin-left "15px"}
     [:i
      {:font-size "42px"
       :text-shadow (str "0 0 5px " ui/color-primary)}]]
    [:.os-cfm-message-text-area
     {:flex "1 1"
      :font-size "13px"
      :padding "0 15px"}]]])

(defn footer []
  [:.os-cfm-footer
   {:height "40%"
    :background (ui/color-primary-darker-rgba "0.4")
    :border-top (str "1px solid" ui/color-primary)}
   [:.os-cfm-button-area
    {:display :flex
     :flex-direction :row
     :align-items :center
     :justify-content :flex-end
     :height "100%"
     :padding-right "15px"}
    [:.primary
     {:background (ui/color-primary-light-rgba "0.7")
      :border (str "1px solid" ui/color-primary-lightest)
      :color ui/color-background}
     [:&:hover
      {:background (ui/color-primary-lighter-rgba "0.8")
       :color "#000"}]]
    [:button
     {:height "26px"}]
    ["button + button"
     {:margin-left "15px"}]]])

(defn local-style []
  [:.app-type-os-confirm
   {}
   [:.os-cfm-container
    {:background (ui/color-primary-darker-rgba "0.2")
     :color ui/color-primary-light
     :height "100%"
     :display :flex
     :flex-direction :column}
    [[(body)]
     [(footer)]]]])
