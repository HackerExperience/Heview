(ns setup.subs
  (:require [re-frame.core :as rf]))

(defn setup
  [db _]
  (:setup db))

(rf/reg-sub
 :setup|loading?
 :<- [:setup]
 (fn [db _]
   (:loading? db)))

(rf/reg-sub
 :setup|boot-failed?
 :<- [:setup]
 (fn [db _]
   (:boot-failed? db)))

