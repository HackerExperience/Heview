(ns web.os.header.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]))

(defn local-style []
  {})

(defn global-style []
  [[:#os-header
    {

     :background-color (ui/color-primary-darkest-rgba "0.50")
     :border-bottom (str "3px solid" ui/color-primary-lightest)
     :box-shadow (str "0 0 20px 5px " (ui/color-primary-darkest-rgba "1.0"))
     :height "50px"

     }]
   [:#os-header-left {}]
   [:#os-header-center {}]
   [:#os-header-right {}]])

