(ns web.apps.chat.lang
  (:require [he.lang]))

(defn _
  [code & args]
  (apply he.lang/_ "web.apps.chat" code args))
