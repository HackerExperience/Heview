(ns web.os.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]
            [web.os.popups.style :as popups.style]))

(defn local-style []
  [])

(defn global-style []
  [[:#os
    {:font-size "13px"
     :height "100%"
     :width "100%"
     :left 0
     ;:line-height 1
     :margin 0
     :overflow :hidden
     :padding 0
     :position :fixed
     :top 0
     :font-family ui/font-family
     :background-repeat :no-repeat
     :background-position "50% 50%"
     :background-size :cover
     :background-color "#111"
     :background-image "url(../background.jpg)"

     :display :flex
     :flex-direction :column

     ;; Disable user selection
     :-webkit-user-select :none
     :-khtml-user-select :none
     :-moz-user-select :none
     :-ms-user-select :none
     :user-select :none}
    [(popups.style/local-style)]]
   [:#os-bsod
    {:position :fixed
     :min-height "100%"
     :width "100%"
     :background "#0000aa"
     :color "#fff"
     :font-family "'courier', 'Roboto'"
     :font-size "12pt"
     :text-align :center}
    [:.os-bsod-container
     {:margin "150px 20%"
      :font-weight :bold}]
    [:.os-bsod-title
     {:padding "2px 8px"
      :background "#fff"
      :color "#0000aa"
      :font-weight :bold}]
    [:p
     {:text-align :left
      :margin "0 0"}]
    [:p.top
     {:margin-top "30px"}]]])
