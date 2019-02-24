(ns web.hemacs.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]))

(defn hemacs
  [db _]
  (:hemacs db))

(rf/reg-sub
 :web|hemacs|enabled?
 :<- [:web|hemacs]
 (fn [db _]
   (:enabled? db)))

(rf/reg-sub
 :web|hemacs|mode|name
 :<- [:web|hemacs]
 (fn [db _]
   (get-in db [:mode :name])))

(rf/reg-sub
 :web|hemacs|buffer
 :<- [:web|hemacs]
 (fn [db _]
   (:buffer db)))

(rf/reg-sub
 :web|hemacs|last-buffer
 :<- [:web|hemacs]
 (fn [db _]
   (:last-buffer db)))

(rf/reg-sub
 :web|hemacs|output
 :<- [:web|hemacs]
 (fn [db _]
   (:output db)))

(rf/reg-sub
 :web|hemacs|which-key
 :<- [:web|hemacs]
 (fn [db _]
   (:which-key db)))
