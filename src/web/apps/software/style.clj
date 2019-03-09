(ns web.apps.software.style
  (:require [web.apps.software.cracker.style :as crc.style]))

(defn style []
  [(crc.style/style)])
