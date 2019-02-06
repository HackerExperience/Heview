(ns web.ui.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]))

(defn local-style []
  {})

(defn global-style []
  [
   ;; Buttons
   [:.ui-btn-area-large {}
    ["button + button"
     {:margin-left "15px"}]]
   [:.ui-btn-area {}
    ["button + button"
     {:margin-left "10px"}]]
   [:.ui-btn
    {:height "20px"
     :background-color (ui/color-primary-darker-rgba "0.5")
     :border (str "1px solid" ui/color-primary-light)
     :color ui/color-primary-light
     :cursor :pointer}
    [[:&:hover
      {:background-color (ui/color-primary-rgba "0.35")
       :border (str "1px solid" ui/color-primary-lightest)
       :color ui/color-primary-lightest}]]]
   [:.btn-icon
    {:height "26px"
     :width "40px"
     :padding "0"}
    [:i
     {:font-size "16px"}]]
   [:.btn-dual
    {:width "75px"
     :height "26px"
     :position :relative}
    [:i
     {:opacity 0
      :transition "opacity 0.1s"
      :position :absolute
      :top "50%"
      :left "50%"
      :margin-left "-7px"
      :margin-top "-6px"}]
    [:span
     {:transition "opacity 0.25s"}]
    [:&:hover {}
     [:i
      {:margin-top "-8px"
       :font-size "16px"
       :opacity 1
       :transition "opacity 0.25s"}]
     [:span
      {:opacity 0
       :transition "opacity 0.1s"}]]]
   [:.btn-primary
    {:background-color (ui/color-primary-light-rgba "0.7")
     :border (str "1px solid" ui/color-primary-lighter)
     :color ui/color-secondary}
    [:&:hover
     {:background-color ui/color-primary-lighter
      :border (str "1px solid" ui/color-primary-lightest)
      :color ui/color-primary-darker}]]
   [:.ui-tbl-row
    {:line-height "24px"
     :border "1px solid #eee"}]

   ;; Input
   [:.ui-input
    {:border (str "1px solid" ui/color-primary-lighter)
     :background ui/color-primary-darkest
     :color ui/color-primary-light
     :height "24px"
     :font-size "12px"
     :padding "4px 22px 4px 4px"}]

   ;; Scroll
   [:.ui-scroll
    ;; See `docs/TODO`
    {:scrollbar-color (str ui/color-primary ui/color-primary-darker)
     :scrollbar-width :thin
     }]
   [".ui-scroll::-webkit-scrollbar"
    {:width "6px"}]
   [".ui-scroll::-webkit-scrollbar-track"
    {:background ui/color-primary-darker
     :border (str "1px solid" ui/color-primary)
     }]
   [".ui-scroll::-webkit-scrollbar-thumb"
    {:background ui/color-primary
     :border (str "1px solid " ui/color-primary-light)
     }]

   ;; Tooltip
   ["[tip]"
    {:position :relative
     :display :inline-block
     }]
   ["[tip]:after"
    {:content "attr(tip)"
     :position :absolute
     :left "50%"
     :padding "5px"
     :background ui/color-secondary
     :border (str "1px solid" ui/color-primary-light)
     :color ui/color-primary-light
     :text-align :center
     :min-width "80px"
     :white-space :nowrap
     :pointer-events :none
     :z-index 99999
     :opacity 0
     :font-size "12px"

     :box-shadow (str
                  "0 0 7px 3px rgba(0,0,0,0.8)"
                  ","
                  "0 0 10px 6px rgba(0,0,0,0.4)")
     ;; Bottom
     :top "100%"
     :margin-top "9px"
     :transform "translateX(-50%) translateY(0%)"
     }]
   ["[tip]:hover::after"
    {:opacity 1
     :transition "opacity 0.5s ease-out"}]])
