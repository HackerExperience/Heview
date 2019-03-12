(ns web.ui.components.impl.notification-panel.style
  (:require [web.ui.vars :as ui]))

(defn style []
  [[:.ui-c-np-container
    {:position :absolute
     :display :flex
     :flex-direction :column
     :width "250px"
     :border (str "1px solid " ui/color-primary-light)
     :background (ui/color-primary-darkest-rgba "0.7")
     :color ui/color-primary-light}]
   [:.ui-c-np-header
    {:min-height "20px"
     :background (ui/color-background-rgba "0.6")
     :font-size "11px"
     :display :flex
     :align-items :center
     :padding-left "10px"
     :font-family :monospace
     :border-bottom (str "1px solid " ui/color-background)}]
   [:.ui-c-np-entries
    {:flex "1 1 100%"
     :display :flex
     :flex-direction :column
     :max-height "400px"
     :overflow-y :auto
     :overflow-x :hidden
     :background (ui/color-background-rgba "0.7")}]
   [:.ui-c-np-entry
    {:flex "0 0 auto"
     :display :flex
     :flex-direction :row
     :align-items :center
     :padding "5px"
     :background (ui/color-primary-darker-rgba "0.3")
     :border-bottom (str "1px solid " ui/color-primary-dark)
     :cursor :pointer}
    [:&:hover
     {:background (ui/color-primary-darker-rgba "0.5")}
     [:>.ui-c-np-entry-icon
      {:color ui/color-primary-light}]
     [:.ui-c-np-entry-body-text-target
      {:color (ui/color-primary-light-rgba "0.7")}]
     [:.ui-c-np-entry-body-footer
      {:color (ui/color-secondary-light-rgba "0.9")}]]]
   [:.ui-c-np-entry-unread
    {:background (ui/color-primary-darker-rgba "0.5")
     :border-bottom (str "1px solid " (ui/color-primary-rgba "0.7"))}
    [:&:hover
     {:background (ui/color-primary-darker-rgba "0.65")}]
    [:>.ui-c-np-entry-icon
     {:color ui/color-primary-light}]]
   [:.ui-c-np-entry-highlighted
    {:background (ui/color-primary-darker-rgba "0.7")}]
   [:.ui-c-np-entry-icon
    {:min-width "50px"
     :font-size "30px"
     :text-align :center
     :color ui/color-primary}]
   [:.ui-c-np-entry-body
    {:flex "1"
     :display :flex
     :flex-direction :column
     :justify-content :center}]
   [:.ui-c-np-entry-body-text
    {:flex "1"
     :padding-left "2px"}]
   [:.ui-c-np-entry-body-text-action
    {:padding "3px 0"}]
   [:.ui-c-np-entry-body-text-target
    {:font-family :monospace
     :font-size "11px"
     :color (ui/color-primary-light-rgba "0.5")}
    [:>i
     {:text-align :center
      :min-width "10px"}]
    [:>span
     {:padding-left "5px"}]]
   [:.ui-c-np-entry-body-footer
    {:padding "3px 0"
     :color ui/color-secondary
     :font-family :monospace
     :font-size "10px"
     :display :flex
     :align-items :center}]])
