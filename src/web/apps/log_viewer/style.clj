(ns web.apps.log-viewer.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]
            [web.apps.log-viewer.popups.style :as popups.style]))

(defn header []
  [[:.a-lv-header
    {:display :flex
     :flex-direction :row}]
   [:.a-lv-h-flag-area
    {:flex "1 1"
     :height "100%"
     :align-self :center
     :display :flex
     :flex-direction :row}]
   [:.a-lv-h-flag
    {:width "26px"
     :height "26px"
     :position :relative
     :text-align :center}
    [:&:hover
     {:cursor :pointer}]
    [:>i
     {:font-size "20px"
      :position :relative
      :margin-top "3px"}]]
   [:.a-lv-h-flag-disabled
    {:color ui/color-primary-dark}]

   [".a-lv-h-flag + .a-lv-h-flag"
    {:margin-left "6px"}]
   [:.a-lv-h-search-area
    {:flex "0 0"
     :display :flex
     :flex-direction :row
     :align-items :center}]
   [:.a-lv-h-search-filter
    {:flex "0 0"
     :margin-right "5px"
     :font-size "16px"}
    [:&:hover
     {:color ui/color-primary-lightest
      :cursor :pointer}]]
   [:.a-lv-h-search-input
    {:flex "1 1"
     :position :relative}
    [:>input
     {:width "100px"}]
    [:>i
     {:position :absolute
      :top "5px"
      :right "7px"}
     [:&:hover
      {:color ui/color-primary-lightest}]]]])

(defn body []
  [[:.a-lv-body
    {:height "100%"
     :overflow :auto}]
   [:.a-lv-b-entries
    {:display "flex"
     :flex-direction "column"
     :height "100%"
     :overflow :auto}]
   [:.a-lv-b-entry-container
    {:border-bottom (str "1px solid" ui/color-primary)
     :border-right-style :none
     :border-left-style :none}
    ["&:nth-child(odd)"
     {:background "rgba(13, 2, 8, 0.225)"}]
    ["&:nth-child(even)"
     {:background "rgba(0, 0, 0, 0.325)"}]
    ["&:nth-child(odd):hover"
     {:background (ui/color-primary-darkest-rgba "0.6")}]
    ["&:nth-child(even):hover"
     {:background (ui/color-primary-darkest-rgba "0.6")}]]
   [:.a-lv-b-entry
    {:padding "5px 15px"
     :display :flex
     :flex-direction :row
     :min-height "36px"}]
   [:.a-lv-b-entry-date
    {:flex "0 0"
     :display :flex
     :flex-direction :column
     :justify-content :center
     :font-size "11px"
     :color ui/color-primary}]
   [:.a-lv-b-entry-date-dmy
    {:flex "0 0"}]
   [:.a-lv-b-entry-date-hms
    {:flex "0 0"}]
   [:.a-lv-b-entry-separator
    {:margin-left "7px"
     :border-left (str "1px solid " ui/color-primary-darker)}]
   [:.a-lv-b-entry-body
    {:flex "1 1"
     :margin-left "7px"
     :overflow :hidden
     :text-overflow :ellipsis
     :align-self :center
     :white-space :nowrap}]
   ;; The `lv-selected` class modifies the behavior of some of the `lv-entry-*`
   ;; rules defined above. Keep this in mind and be happy!
   [:.a-lv-b-selected-container
    {:min-height "94px"
     :display :flex
     :flex-direction :column
     :background (ui/color-primary-darkest-rgba "0.75")}
    [:&:hover
     {:background (ui/color-primary-darkest-rgba "0.85")}]
    [:>.a-lv-b-entry
     {:border-bottom :none
      :flex-grow 1}
     [:>.a-lv-b-entry-body
      {:white-space :normal}]]]
   [:.a-lv-b-selected-separator
    {:width "80%"
     :margin-left :auto
     :margin-right :auto
     :border-top (str "1px solid " ui/color-primary-darker)}]
   [:.a-lv-b-selected-action-area
    {:min-height "42px"
     :display :flex
     :flex-direction :row
     :align-items :center
     :justify-content :center}]])

(defn footer []
  [:.a-lv-footer
   {:display :flex
    :flex-direction :row
    :align-items :center}])

(defn style []
  [[:.a-lv-container
    {:height "100%"
     :display :flex
     :flex-direction :column}]
   [(header)]
   [(body)]
   [(footer)]
   [(popups.style/style)]])
