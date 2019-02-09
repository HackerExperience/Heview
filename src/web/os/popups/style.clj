(ns web.os.popups.style
  (:require [web.os.popups.confirm.style :as confirm.style]
            [web.os.popups.os-error.style :as os-error.style]))

(defn local-style []
  [[(confirm.style/local-style)]
   [(os-error.style/local-style)]])
