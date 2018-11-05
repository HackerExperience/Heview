(ns web.home.login.db
  (:require [cljs.spec.alpha :as s]))


(s/def ::username (s/and string? #(<= 1 (count %) 20)))

;; (s/def ::preallocated boolean?)

;; (def supported-types #{"multihomed" "stub" "transit" "ixp"})
;; (s/def ::type supported-types)

;; (s/def ::tags (s/coll-of string?))

;; (s/def ::asn (s/and int? #(<= 1 % 65536)))
;; (s/def ::first ::asn)
;; (s/def ::last ::asn)
;; (s/def ::range (s/and (s/keys :req-un [::first ::last])
;;                       (fn [{:keys [first last]}]
;;                         (< first last))))

;; (s/def ::ranges (s/coll-of ::range :distinct true :min-count 1))

(s/def ::create-payload
  (s/keys :req-un [::label]))

(def form-initial
  {:username ""
   :password ""
   :error ""})

(def initial
  {:form form-initial})
