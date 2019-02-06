(ns web.lock.subs
  (:require [cljs.core.match :refer-macros [match]]
            [re-frame.core :as rf]))

(defn lock
  [db _]
  (:lock db))

(rf/reg-sub
 :web|lock|loading?
 :<- [:web|lock]
 (fn [db _]
   (:loading? db)))

(rf/reg-sub
 :web|lock|fail-reason
 :<- [:web|lock]
 (fn [db _]
   (match (:fail-reason db)
          :expired "Expired"
          :internal "Internal"
          :else "IDK")))

(rf/reg-sub
 :web|lock|failed?
 :<- [:web|lock]
 (fn [db _]
   (some? (:fail-reason db))))

(rf/reg-sub
 :web|lock|form
 :<- [:web|lock]
 (fn [db _]
   (:form db)))

(rf/reg-sub
 :web|lock|form|password
 :<- [:web|lock|form]
 (fn [db _]
   (:password db)))

(rf/reg-sub
 :web|lock|form|error
 :<- [:web|lock|form]
 (fn [db _]
   (match (:error db)
          :not_found "Invalid user or password"
          :internal "Internal"
          :else "IDK")))

(rf/reg-sub
 :web|lock|form|has-error?
 :<- [:web|lock|form|error]
 (fn [error _]
   (some? error)))

