(ns game.story.lang
  (:require [he.lang]))

(defn _
  [code & args]
  (apply he.lang/_ "game.story" code args))
