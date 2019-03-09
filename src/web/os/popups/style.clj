(ns web.os.popups.style
  (:require [web.os.popups.confirm.style :as confirm.style]
            [web.os.popups.os-error.style :as os-error.style]))

(defn style []
  [[(confirm.style/style)]
   [(os-error.style/style)]])
