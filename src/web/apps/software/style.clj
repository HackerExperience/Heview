(ns web.apps.software.style
  (:require [web.apps.software.cracker.style :as crc.style]))

(defn local-style []
  [(crc.style/local-style)])
