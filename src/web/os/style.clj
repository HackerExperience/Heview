(ns web.os.style
  (:require [garden.core :refer [css]]
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
     :font-family "'Roboto', sans-serif"
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
    [(popups.style/local-style)]]])
