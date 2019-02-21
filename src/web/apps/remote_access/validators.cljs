(ns web.apps.remote-access.validators
  (:require [taoensso.truss :refer-macros [have?]]
            [he.validators :as he.v]
            [he.utils.validators :as utils.v]))

(defn v [fun x]
  (utils.v/v fun x))

(defn data-remote [x]
  true)

(defn data-auth [x]
  (and (have? [:ks>= #{:user :pass :loading? :valid? :show-validation?}] x)
       (have? string? (:user x))
       (have? string? (:pass x))
       (have? boolean? (:valid? x))
       (have? boolean? (:show-validation? x))
       (have? boolean? (:loading? x))))

(defn data-browse [x]
  (and (have? [:ks>= #{:loading? :valid? :show-validation?}])
       (have? boolean? (:valid? x))
       (have? boolean? (:show-validation? x))
       (have? boolean? (:loading? x))))

(defn screen [x]
  (have? (utils.v/in? [:browse :auth :remote] x)))

(defn ip [x]
  (have? string? x))

(defn state [x]
  (and (have? [:ks>= #{:server-cid :screen :ip
                       :data-browse :data-auth :data-remote}] x)
       (he.v/have-server-cid? (:server-cid x))
       (ip (:ip x))
       (screen (:screen x))
       (data-browse (:data-browse x))
       (data-auth (:data-auth x))
       (data-remote (:data-remote x))))
