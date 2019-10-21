(ns web.ui.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]
            [web.ui.components.style :as components.style]))

(defn style []
  [;; Buttons
   [:.ui-btn-area-large
    ["button + button"
     {:margin-left "15px"}]]
   [:.ui-btn-area
    ["button + button"
     {:margin-left "10px"}]]
   [:.ui-btn
    {:height "20px"
     :background-color (ui/color-primary-darker-rgba "0.5")
     :border (str "1px solid" ui/color-primary-light)
     :color ui/color-primary-light
     :cursor :pointer}
    [:&:hover
     {:background-color (ui/color-primary-rgba "0.35")
      :border (str "1px solid" ui/color-primary-lightest)
      :color ui/color-primary-lightest}]]
   [:.ui-btn-disabled
    {:color ui/color-primary
     :border (str "1px solid " ui/color-primary)
     :background-color (ui/color-primary-darker-rgba "0.35")
     :cursor :initial}
    [:&:hover
     {:color ui/color-primary
      :border (str "1px solid " ui/color-primary)
      :background-color (ui/color-primary-darker-rgba "0.35")}]]
   [:.ui-btn-icon
    {:height "26px"
     :width "40px"
     :padding "0"
     :font-size "16px"}]
   [:.ui-btn-dual
    {:width "75px"
     :height "26px"
     :position :relative}
    [:&:hover {}
     [:>i
      {:margin-top "-8px"
       :font-size "16px"
       :opacity 1
       :transition "opacity 0.25s"}]
     [:>span
      {:opacity 0
       :transition "opacity 0.1s"}]]
    [:>i
     {:opacity 0
      :transition "opacity 0.1s"
      :position :absolute
      :top "50%"
      :left "50%"
      :margin-left "-7px"
      :margin-top "-6px"}]
    [:>span
     {:transition "opacity 0.25s"}]]
   [:.ui-btn-primary
    {:background-color (ui/color-primary-light-rgba "0.45")
     :border (str "1px solid" ui/color-primary-lightest)
     :text-shadow (str "0 0 " ui/color-primary)
     :color ui/color-background}
    [:&:hover
     {:background-color ui/color-primary-light
      :border (str "1px solid" ui/color-primary-lightest)
      :color ui/color-primary-darker}]]

   ;; Spinner
   [:.ui-spinner-area
    {:cursor :wait}]

   ;; Input
   [:.ui-input
    {:border (str "1px solid" ui/color-primary-light)
     :background (ui/color-primary-darkest-rgba "0.5")
     :color ui/color-primary-light
     :height "24px"
     :font-size "12px"
     :padding "4px 22px 4px 4px"
     :width "150px"
     :text-decoration :none}
    [:&:hover
     {:background (ui/color-primary-darker-rgba "0.4")
      :border (str "1px solid" ui/color-primary-lighter)}]
    [:&:focus
     {:background ui/color-primary-darkest
      :border (str "1px solid" ui/color-primary-lightest)}]]

   ;; Scroll
   ;; Scroll > Firefox
   ["*"
    {:scrollbar-color (str ui/color-primary ui/color-primary-darker)
     :scrollbar-width :thin}]
   ;; Scroll > Chrome
   ["::-webkit-scrollbar"
    {:width "6px"}]
   ["::-webkit-scrollbar-track"
    {:background ui/color-primary-darker
     :border (str "1px solid" ui/color-primary)}]
   ["::-webkit-scrollbar-thumb"
    {:background ui/color-primary
     :border (str "1px solid " ui/color-primary-light)}]
   ["::-webkit-scrollbar-thumb:active"
    {:background (ui/color-primary-light-rgba "0.525")
     :border (str "1px solid " ui/color-primary-lightest)}]

   ;; Help
   [:.ui-help
    {:display :block
     :margin "0 5px"
     :font-size "12px"}
    [:>i
     {:color ui/color-primary-dark
      :margin-top "2px"
      :padding "3px 2px"}
     [:&:hover
      {:color ui/color-primary}]]]

   ;; Tooltip
   ["[tip]"
    {:position :relative
     :display :inline-block}]
   ["[tip]:after"
    {:content "attr(tip)"
     :position :absolute
     :left "50%"
     :padding "5px"
     :background ui/color-background
     :border (str "1px solid" ui/color-primary-light)
     :color ui/color-primary-light
     :text-align :center
     :min-width "80px"
     :white-space :nowrap
     :pointer-events :none
     :z-index 999999
     :opacity 0
     :font-size "12px"
     :font-family ui/font-family
     :font-weight :normal
     :box-shadow (str
                  "0 0 7px 3px rgba(0,0,0,0.8)"
                  ","
                  "0 0 10px 6px rgba(0,0,0,0.4)")
     :top "100%"
     :margin-top "9px"
     :transform "translateX(-50%) translateY(0%)"
     ;; Fix "blurriness" on chrome
     "-webkit-backface-visibility" "hidden"}]
   ["[tip]:hover::after"
    {:opacity 1
     :transition "opacity 0.5s ease-out"}]

   ;; Tab
   [:.ui-tab-area
    {:display :flex
     :flex-direction :row
     :min-height "40px"
     :margin "7px 0"}]
   [:.ui-tab-entry
    {:display :flex
     :flex-direction :row
     :align-items :center
     :padding "0 10px"
     :justify-content :center
     :border-top "2px solid transparent"
     :border-left "1px solid transparent"
     :border-right "1px solid transparent"
     :border-bottom (str "1px solid " ui/color-primary-dark)
     :color ui/color-primary}
    [:&:hover
     {:cursor :pointer
      :color ui/color-primary-light
      :border-bottom (str "1px solid " ui/color-primary-light)}]]
   [:.ui-tab-entry-icon
    {:min-width "15px"
     :text-align :center
     :font-size "12px"}]
   [:.ui-tab-entry-text
    {:margin-left "3px"
     :font-size "15px"}]
   [:.ui-tab-selected
    {:border-bottom "1px solid transparent"
     :border-top (str "2px solid " ui/color-primary-light)
     :border-left (str "1px solid " ui/color-primary-dark)
     :border-right (str "1px solid " ui/color-primary-dark)
     :color ui/color-primary-light}
    [:&:hover
     {:cursor :initial
      :border-bottom "1px solid transparent"}
     [:.ui-tab-entry-icon
      {:color (ui/color-primary-light-rgba "0.8")}]]]
   [:.ui-tab-entry-icon
    {:color (ui/color-primary-light-rgba "0.8")}]
   [:.ui-tab-pre
    {:border-bottom (str "1px solid " ui/color-primary-dark)
     :min-width "10px"}]
   [:.ui-tab-rest
    {:flex "1 1"
     :border-bottom (str "1px solid " ui/color-primary-dark)}]

   ;; UI Global
   [:.ui-g-bold
    {:font-weight :bold}]

   ;; Fonts
   ["@font-face"
    {:font-family "profont"
     :src "url('../fonts/profont.woff') format('woff')"}]

   [(components.style/style)]])
