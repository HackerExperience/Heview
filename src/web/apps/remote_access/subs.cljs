(ns web.apps.remote-access.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]))

(rf/reg-sub
 :web|apps|remote-access|screen
 he/with-app-state
 (fn [[state] _]
   (:screen state)))

(rf/reg-sub
 :web|apps|remote-access|ip
 he/with-app-state
 (fn [[state]]
   (:ip state)))

;; Browse ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn with-browse-data-callback
  [[_ app-id]]
  [(he/subscribed [:web|apps|remote-access|browse|data app-id])])

(def with-browse-data
  #(with-browse-data-callback %))

(rf/reg-sub
 :web|apps|remote-access|browse|data
 he/with-app-state
 (fn [[state]]
   (:data-browse state)))

(rf/reg-sub
 :web|apps|remote-access|browse|loading?
 with-browse-data
 (fn [[data]]
   (:loading? data)))

;; Auth ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn with-auth-data-callback
  [[_ app-id]]
  [(he/subscribed [:web|apps|remote-access|auth|data app-id])])

(def with-auth-data
  #(with-auth-data-callback %))

(rf/reg-sub
 :web|apps|remote-access|auth|data
 he/with-app-state
 (fn [[state]]
   (:data-auth state)))

(rf/reg-sub
 :web|apps|remote-access|auth|pass
 with-auth-data
 (fn [[data]]
   (:pass data)))

(rf/reg-sub
 :web|apps|remote-access|auth|loading?
 with-auth-data
 (fn [[data]]
   (:loading? data)))
