(ns web.setup.subs
  (:require [re-frame.core :as rf]))

(defn setup
  [db _]
  (:setup db))

(rf/reg-sub
 :web|setup|loading?
 :<- [:web|setup]
 (fn [db _]
   (:loading? db)))

(rf/reg-sub
 :web|setup|boot-failed?
 :<- [:web|setup]
 (fn [db _]
   (:boot-failed? db)))

