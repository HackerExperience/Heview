(ns web.hemacs.lang
  (:require [he.lang]))

(defn _
  [code & args]
  (apply he.lang/_ "web.hemacs" code args))
