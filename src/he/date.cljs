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
