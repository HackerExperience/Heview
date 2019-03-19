(ns web.boot.style
  (:require [web.ui.vars :as ui]))

(defn style []
  [[:.boot
    {:display :flex
     :flex-direction :column
     :background-color "#000"
     :color "#fff"
     :height "100vh"
     :font-family ui/font-boot
     :font-size "14px"
     }]
   [:.boot-row1
    {:flex "1 1"}]
   [:.boot-row1-text
    {:display :flex
     :flex-direction :column
     :position :absolute
     :top "20px"
     :right "20px"
     :text-align :right}
    [:>span
     {:padding-bottom "5px"}]]
   [:.boot-row2
    {:flex "1 1"
     :display :flex
     :flex-direction :row
     :align-items :center
     :justify-content :center}]
   [:.boot-row2-splash
    {:background "url(../splash.png) center no-repeat"
     ;; :flex "1 1"
     :min-width "700px"
     :min-height "250px"
     :align-self :center
     }]
   [:.boot-row3
    {:flex "1 1"
     :position :relative
     :display :flex
     :flex-direction :row
     :align-items :center
     :justify-content :center}
    [:>span
     {:position :absolute
      :font-size "16px"
      :bottom "20px"}]]])
