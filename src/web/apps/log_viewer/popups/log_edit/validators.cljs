(ns web.apps.log-viewer.popups.log-edit.validators
  (:require [taoensso.truss :refer-macros [have?]]
            [he.validators :as he.v]
            [he.utils.validators :as utils.v]))

(defn v [fun x]
  (utils.v/v fun x))

(defn invalid-fields [x]
  (and (have? vector? x)
       (some #(have? keyword? %) x)))

(defn state [x]
  (and (have? [:ks>= #{:server-cid :log-id :log :invalid-fields :changed?}] x)
       (he.v/have-server-cid? (:server-cid x))
       (he.v/have-helix-id? (:log-id x))
       (have? any? (:log x)) ;; TODO: Create on `game.server.log` and reuse
       (invalid-fields (:invalid-fields x))
       (have? boolean? (:changed? x))))
