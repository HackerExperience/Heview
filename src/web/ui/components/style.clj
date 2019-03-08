(ns web.ui.components.style
  (:require [web.ui.components.impl.dropdown.style :as dropdown]))

(defn local-style []
  [(dropdown/local-style)])
