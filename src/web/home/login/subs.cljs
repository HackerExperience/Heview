(ns web.home.login.subs
  (:require [re-frame.core :as rf]))

(defn login
  [db _]
  (:login db))

(rf/reg-sub
 :web|home|login|form
 :<- [:web|home|login]
 (fn [db _]
   (:form db)))

(rf/reg-sub
 :web|home|login|form|username
 :<- [:web|home|login|form]
 (fn [db _]
   (:username db)))

(rf/reg-sub
 :web|home|login|form|password
 :<- [:web|home|login|form]
 (fn [db _]
   (:password db)))

(rf/reg-sub
 :web|home|login|form|error
 :<- [:web|home|login|form]
 (fn [db _]
   (:error db)))

(rf/reg-sub
 :web|home|login|form|has-error?
 :<- [:web|home|login|form|error]
 (fn [error _]
   (not (clojure.string.blank? error))))

