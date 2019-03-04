(ns he.utils
  (:require [clojure.string :as str]
            ["ip-address" :as ip-address]))

(defn vec-remove
  "Removes the entry from vector `vector` at index `index`"
  [vector index]
  (vec
   (concat
    (subvec vector 0 index)
    (subvec vector (inc index)))))

;; Taken from https://stackoverflow.com/a/14488425/1454986
(defn dissoc-in
  "Dissociates an entry from a nested associative structure returning a new
  nested structure. keys is a sequence of keys. Any empty maps that result
  will not be present in the new structure."
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (if (seq newmap)
          (assoc m k newmap)
          (dissoc m k)))
      m)
    (dissoc m k)))

(defn get-canonical-id
  [ipv6-id]
  (.canonicalForm (new ip-address/Address6 (name ipv6-id))))

(defn valid-ipv4?
  [ipv4]
  (.isValid (new ip-address/Address4 ipv4)))

(defn valid-file-name?
  [file-name]
  (if (str/blank? file-name)
    false
    (not (.test #"[^a-zA-Z0-9]" file-name))))

(defn in?
  [coll element]
  (some #(= element %) coll))

(defn conj-at-pos
  [pos coll element]
  (let [[before after] (split-at pos coll)
        before (into [] before)
        after (into [] after)]
    (let [nested-vector (vector before [element] after)]
      (apply concat nested-vector))))
