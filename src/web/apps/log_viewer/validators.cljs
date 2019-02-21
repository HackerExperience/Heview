(ns web.apps.log-viewer.validators
  (:require [taoensso.truss :refer-macros [have?]]
            [he.validators :as he.v]
            [he.utils.validators :as utils.v]))

(defn v [fun x]
  (utils.v/v fun x))

(defn selected [x]
  (utils.v/have-true?
   (or false
       (nil? x)
       (he.v/is-helix-id? x))))

(defn state [x]
  (selected (:selected x)))
