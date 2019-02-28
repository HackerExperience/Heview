(ns he.utils.str
  (:require [goog.string :as gstring]
            [goog.string.format]))

(defn format
  [formatter number]
  (gstring/format formatter number))
