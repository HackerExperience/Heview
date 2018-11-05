(ns web.home.login.subs
  (:require [re-frame.core :as rf]))

(defn login
  [db _]
  (:login db))

(rf/reg-sub
 :home|login|form
 :<- [:home|login]
 (fn [db _]
   (:form db)))

(rf/reg-sub
 :home|login|form|username
 :<- [:home|login|form]
 (fn [db _]
   (:username db)))

(rf/reg-sub
 :home|login|form|password
 :<- [:home|login|form]
 (fn [db _]
   (:password db)))

(rf/reg-sub
 :home|login|form|error
 :<- [:home|login|form]
 (fn [db _]
   (:error db)))

(rf/reg-sub
 :home|login|form|has-error?
 :<- [:home|login|form|error]
 (fn [error _]
   (not (clojure.string.blank? error))))

