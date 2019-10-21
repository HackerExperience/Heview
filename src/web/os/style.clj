(ns web.os.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]
            [web.os.popups.style :as popups.style]))

(defn style []
  [[:#os
    {:font-size "13px"
     :height "100%"
     :width "100%"
     :left 0
     :margin 0
     :overflow :hidden
     :padding 0
     :position :fixed
     :top 0
     :font-family ui/font-family
     :background-repeat :no-repeat
     :background-position "50% 50%"
     :background-size :cover
     :background-color "#050505"
     ;; :background-image "url(../background.jpg)"
     :display :flex
     :flex-direction :column}]
   [:#os-bsod
    {:position :fixed
     :min-height "100%"
     :width "100%"
     :background "#0000aa"
     :color "#fff"
     :font-family "'courier', 'Roboto'"
     :font-size "12pt"
     :text-align :center}]
   [:.os-bsod-container
    {:margin "150px 20%"
     :font-weight :bold}
    [:>p
     {:text-align :left
      :margin "0 0"}]
    [:>.top
     {:margin-top "30px"}]]
   [:.os-bsod-title
    {:padding "2px 8px"
     :background "#fff"
     :color "#0000aa"
     :font-weight :bold}]
   [:.os-bsod-line-top
    {:margin-top "30px"}]
   [(popups.style/style)]])
