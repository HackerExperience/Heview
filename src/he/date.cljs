(ns he.date
  (:require [cljs.core.match :refer-macros [match]]
            [cljs-time.format])
  (:use [cljs-time.coerce :only [from-long]]
        [cljs-time.format :only [unparse]]))

;; (defn timestamp-to-datetime
;;   [timestamp]
;;   (from-long timestamp))

(defn timestamp-to-datetime
  [timestamp]
  (new js/Date timestamp))

(defn format
  [datetime formatter]
  "todo"
  ;; (unparse formatter datetime)
  )

(defn formatter
  [format-str]
  (cljs-time.format/formatter format-str))

(defn format-uptime
  [diff]
  (let [m (mod (int (/ diff 60)) 60)
        h (mod (int (/ diff 3600)) 3600)
        d (mod (int (/ diff 86400)) 86400)
        m-str (str m "m")
        h-str (when (pos? h) (str h "h"))
        d-str (when (pos? d) (str d "d"))]
    (str d-str h-str m-str)))

;; JS.Date utils

(defn format-day-numeric
  [date]
  (let [day (.getDate date)]
    (if (< day 10)
      (str "0" day)
      (str day))))

(defn format-month-numeric
  [date]
  (let [month (inc (.getMonth date))]
    (if (< month 10)
      (str "0" month)
      (str month))))

(defn format-month-word
  [date]
  (let [month (inc (.getMonth date))]
    (match month
           1 "January"
           2 "February"
           3 "March"
           4 "April"
           5 "May"
           _ "Todo")))

(defn format-hour-numeric
  [date]
  (let [hour (.getHours date)]
    (if (< hour 10)
      (str "0" hour)
      (str hour))))

(defn format-minute-numeric
  [date]
  (let [minute (.getMinutes date)]
    (if (< minute 10)
      (str "0" minute)
      (str minute))))

;; Utils

;; Localization is a huge todo
(defn to-time-ago-old
  [ts-past]
  (let [date (new js/Date ts-past)
        month (format-month-word date)
        day (format-day-numeric date)
        hour (format-hour-numeric date)
        minute (format-minute-numeric date)]
    (str month ", " day " at " hour ":" minute)))

(defn to-time-ago-yesterday
  [ts-past]
  (let [date (new js/Date ts-past)
        hour (format-hour-numeric date)
        minute (format-minute-numeric date)]
    (str "Yesterday at " hour ":" minute)))

(defn to-time-ago
  [ts-now ts-past]
  (let [diff (int (/ (- ts-now ts-past) 1000))]
    (cond
      (<= diff 59) "Now"
      (<= diff 119) "1 minute ago"
      (<= diff 3599) (str (int (/ diff 60)) " minutes ago")
      (<= diff 7199) "1 hour ago"
      (<= diff 86399) (str (int (/ diff 60 60)) " hours ago")
      (<= diff 172800) (to-time-ago-yesterday ts-past)
      :else (to-time-ago-old ts-past))))

(defn milliseconds-till-next-minute
  "Returns the total milliseconds until the next second arrives. Example:
  Current time: HH:00:54.250
  Next minute: HH:01:00.00
  Milliseconds till next minute: 5750 (+15 for lag purposes / frame diff)"
  []
  (let [now (new js/Date)]
    (-> now
        (.getSeconds)
        (* 1000)
        (->> (- 60000))
        (- (.getMilliseconds now))
        (+ 15))))
