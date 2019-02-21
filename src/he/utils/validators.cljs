(ns he.utils.validators
  (:require [taoensso.truss :refer-macros [have?]]))

(defn v [fun x]
  (fun x)
  x)

(defn v-each [fun seq-args]
  (doseq [x seq-args]
    (fun x)))

(defn in? [coll elem]
  (some #(= elem %) coll))

(defn have-true? [x]
  (have? true? x))

(defn have-false? [x]
  (have? false? x))

;; (defn pos-int? [x]
;;   (and (int? x)
;;        (pos? x)))
