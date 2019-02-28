(ns he.date
  (:require [cljs-time.format])
  (:use [cljs-time.coerce :only [from-long]]
        [cljs-time.format :only [unparse]]))

(defn timestamp-to-datetime
  [timestamp]
  (from-long timestamp))

(defn format
  [datetime formatter]
  (unparse formatter datetime))

(defn formatter
  [format-str]
  (cljs-time.format/formatter format-str))

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
