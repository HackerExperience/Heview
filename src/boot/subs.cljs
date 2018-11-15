(ns boot.subs
  (:require [re-frame.core :as rf]))

(defn boot
  [db _]
  (:boot db))

(rf/reg-sub
 :boot|loading?
 :<- [:boot]
 (fn [db _]
   (:loading? db)))

(rf/reg-sub
 :boot|failed?
 :<- [:boot]
 (fn [db _]
   (:failed? db)))

