(ns web.os.popups.style
  (:require [web.os.popups.confirm.style :as confirm.style]))

(defn local-style []
  [[(confirm.style/local-style)]])
