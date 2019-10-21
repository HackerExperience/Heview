(ns web.hud.conky.widgets.system.db
  (:require [he.date]))

(defn get-uptime-diff
  [boot-time]
  (int (/ (- (.now js/Date) boot-time) 1000)))
