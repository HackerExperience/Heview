(ns web.apps.log-viewer.popups.log-edit.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]))

(defn body []
  [[:.a-lv-led-body
    {:flex "1 1"
     :display :flex
     :flex-direction :column}]
   [:.a-lv-led-b-preview-area
    {:padding "10px 15px"
     :min-height "100px"
     :display :flex}
    [:>fieldset
     {:flex "1 1"
      :background (ui/color-primary-darkest-rgba "0.5")
      :border (str "1px solid " ui/color-primary-dark)
      :display :flex
      :min-height "100%"
      :font-size "12px"}]]
   [:.a-lv-led-b-preview-area
    [:>.a-lv-led-b-preview-area-corrupt
     {:border (str "1px solid " ui/color-secondary-dark)}
     [:>legend
      {:color ui/color-secondary-light}]]]
   [:.a-lv-led-b-preview-wrapper
    {:padding "5px 3px"
     :flex "1 1"
     :height "54px"
     :display :flex
     :flex-direction :row
     :align-items :center}]
   [:.a-lv-led-b-preview-icon
    {:flex "0 0"
     :height "100%"}
    [:>i
     {:font-size "20px"
      :margin-top "13px"}]]
   [:.a-lv-led-b-preview-icon-success
    {:color ui/color-primary}
    [:&:hover
     {:color ui/color-primary-light}]]
   [:.a-lv-led-b-preview-icon-corrupt
    {:color ui/color-secondary}
    [:&:hover
     {:color ui/color-secondary-light}]]
   [:.a-lv-led-b-preview-text
    {:flex "1 1"
     :font-size "13px"
     :height "100%"
     :margin-left "8px"
     :display :flex
     :align-items :center
     :overflow-y :auto
     :word-break :break-word}]
   [:.a-lv-led-b-section-separator
    {:height "1px"
     :width "100%"
     :margin-left :auto
     :margin-right :auto
     :margin-top "5px"
     :margin-bottom "5px"
     :border-top (str "1px dotted" ui/color-primary)}]
   [:.a-lv-led-b-edit-area
    {:height "100%"
     :padding "5px 15px"
     :display :flex
     :flex-direction :column}]
   [:.a-lv-led-b-edit-type
    {:min-height "40px"
     :flex "0 0"
     :display :flex
     :flex-direction :row
     :align-items :center}
    [:>span
     {:min-width "90px"}]]
   [:.a-lv-led-b-edit-type-select
    {:width "100%"
     :margin-right "2px"
     :margin-left :auto
     :display :flex
     :flex-direction :row
     :justify-content :flex-end}]
   [:.a-lv-led-b-edit-fields
    {:flex "1 1"
     :margin-top "6px"
     :display :flex
     :flex-direction :column}
    [:>fieldset
     {:border (str "1px solid " ui/color-primary-dark)}]]
   [:.a-lv-led-b-edit-field-row
    {:min-height "34px"
     :display :flex
     :flex-direction :row
     :align-items :center}
    [:&:hover
     {:background (ui/color-primary-darkest-rgba "0.5")}]]
   [:.a-lv-led-b-edit-field-help
    {:margin-right "2px"
     :cursor :initial}]
   [:.a-lv-led-b-edit-field-text
    {:width "150px"
     :margin-left "2px"}]
   [:.a-lv-led-b-edit-field-input-area
    {:position :relative}
    [:i
     {:position :absolute
      :color ui/color-secondary-light
      :right "17px"
      :top "5px"}
     [:&:hover
      {:cursor :initial
       :color ui/color-secondary-lightest}]]]
   [:.a-lv-led-b-edit-field-input
    {:margin-right "10px"}]
   [:.a-lv-led-b-edit-field-content-invalid
    {:border (str "1px solid " ui/color-secondary-light)}
    [:&:hover
     {:border (str "1px solid " ui/color-secondary-lighter)}]]
   [".a-lv-led-b-edit-field-row + .a-lv-led-b-edit-field-row"
    {:margin-top "10px"}]
   [:.a-lv-led-b-edit-type-dd-container
    {:min-width "250px"}]])

(defn footer []
  [[:.a-lv-led-footer
    {:flex "0 0"
     :display :flex
     :flex-direction :row
     :align-items :center}]
   [:.a-lv-led-f-alternative-buttons
    {:flex "0 0"
     :min-width "140px"}
    ["button + button"
     {:margin-left "10px"}]]
   [:.a-lv-led-f-main-buttons
    {:flex "1 1"
     :display :flex
     :flex-direction :row
     :justify-content :flex-end}
    ["button + button"
     {:margin-left "10px"}]]])

(defn style []
  [[:.a-lv-led-container
    {:height "100%"
     :display :flex
     :flex-direction :column}]
   [(body)]
   [(footer)]])
