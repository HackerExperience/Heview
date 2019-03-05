(ns web.apps.software.cracker.style
  (:require [web.ui.vars :as ui]))

(defn bruteforce-body []
  [:.crc-bf-body
   {:flex "1 1"
    :display :flex
    :flex-direction :column
    :padding "0 7px 7px 7px"
    ;; :background-color (ui/color-background-rgba "0.2")
    }
   [:.crc-bf-fields
    {:flex "1 1"
     :display :flex
     :flex-direction :column}
    [:.crc-bf-field
     {:display :flex
      :flex-direction :row
      :padding "5px 5px 5px 0"
      :min-height "34px"
      :align-items :center}
     [:&:hover
      {:background-color (ui/color-primary-darkest-rgba "0.5")}]
     [:.crc-bf-field-label
      {:flex "1 1"}]
     [:.crc-bf-field-input-area
      {:width "150px"
       :position :relative}
      [:.crc-bf-field-input
       {:width "140px"
        :right "0"
        :top "-12px"
        :position :absolute
        :font-family :monospace}]]]
    [".crc-bf-field + .crc-bf-field"
     {:margin-top "5px"}]]])

(defn bruteforce-footer []
  [:.crc-bf-footer
   {:min-height "42px"
    :border-top (str "1px solid " ui/color-primary-darker)
    :background-color (ui/color-primary-darkest-rgba "0.35")
    :display :flex
    :flex-direction :row
    :align-items :center}
   [:.crc-bf-action-area
    {:flex "1 1"
     :display :flex
     :flex-direction :row
     :padding "3px 20px"}
    [:.crc-bf-action-left
     {:margin-right :auto}]
    [:.crc-bf-action-right
     {:margin-left :auto}]]])

(defn bruteforce []
  [:.crc-bf-container
   {:height "100%"
    :flex "1 1"
    :display :flex
    :flex-direction :column}
   [[(bruteforce-body)]
    [(bruteforce-footer)]]])

(defn overflow []
  [])

(defn local-style []
  [:.app-type-software-cracker
   [:.crc-container
    {:min-height "100%"
     :background-color (ui/color-background-rgba "0.3")
     :display :flex
     :flex-direction :column}
    [[(bruteforce)]
     [(overflow)]]]])
