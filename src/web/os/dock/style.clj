(ns web.os.dock.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]))

(defn local-style []
  {})

(defn global-style []
  [[:#os-dock
    {:border-top (str "3px solid" ui/color-primary-lightest)
     :box-shadow (str "0 0 20px 5px " (ui/color-primary-darkest-rgba "1.0"))
     :background-color (ui/color-primary-darkest-rgba "0.35")
     :height "50px"}]])
