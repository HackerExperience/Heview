(ns he.validators
  (:require [taoensso.truss :refer-macros [have?]]
            [he.utils.validators :as utils.v]))

;; Maybe `game.validators`?

(defn is-server-cid? [x]
  (string? x))

(defn have-server-cid? [x]
  (utils.v/have-true? (is-server-cid? x)))

(defn is-helix-id? [x]
  (string? x))

(defn have-helix-id? [x]
  (utils.v/have-true? (is-helix-id? x)))

