(ns web.apps.software.cracker.style
  (:require [web.ui.vars :as ui]))

(defn bruteforce-body []
  [[:.a-crc-bf-body
    {:flex "1 1"
     :display :flex
     :flex-direction :column
     :padding "0 7px 7px 7px"}]
   [:.a-crc-bf-b-fields
    {:flex "1 1"
     :display :flex
     :flex-direction :column}]
   [:.a-crc-bf-b-field
    {:display :flex
     :flex-direction :row
     :padding "5px 5px 5px 0"
     :min-height "34px"
     :align-items :center}
    [:&:hover
     {:background-color (ui/color-primary-darkest-rgba "0.5")}]]
   [:.a-crc-bf-b-field-label
    {:flex "1 1"}]
   [:.a-crc-bf-b-field-input-area
    {:width "150px"
     :position :relative}]
   [:.a-crc-bf-b-field-input
    {:width "140px"
     :right "0"
     :top "-12px"
     :position :absolute
     :font-family :monospace}]
   [".a-crc-bf-b-field + .a-crc-bf-b-field"
    {:margin-top "5px"}]])

(defn bruteforce-footer []
  [[:.a-crc-bf-footer
    {:min-height "42px"
     :border-top (str "1px solid " ui/color-primary-darker)
     :background-color (ui/color-primary-darkest-rgba "0.35")
     :display :flex
     :flex-direction :row
     :align-items :center}]
   [:.a-crc-bf-f-action-area
    {:flex "1 1"
     :display :flex
     :flex-direction :row
     :padding "3px 20px"}]
   [:.a-crc-bf-f-action-left
    {:margin-right :auto}]
   [:.a-crc-bf-f-action-right
    {:margin-left :auto}]])

(defn bruteforce []
  [[:.a-crc-bf-container
    {:height "100%"
     :flex "1 1"
     :display :flex
     :flex-direction :column}]
   [(bruteforce-body)]
   [(bruteforce-footer)]])

(defn overflow []
  [])

(defn style []
  [[:.a-crc-container
    {:min-height "100%"
     :background-color (ui/color-background-rgba "0.3")
     :display :flex
     :flex-direction :column}]
   [(bruteforce)]
   [(overflow)]])
