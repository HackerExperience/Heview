(ns web.os.subs
  (:require [re-frame.core :as rf]
            [web.os.popups.subs]))

(defn os
  [db _]
  (:os db))

(rf/reg-sub
 :web|os|error|runtime
 :<- [:web|os]
 (fn [db _]
   (:runtime-error db)))

(rf/reg-sub
 :web|os|boot-time
 :<- [:web|os]
 (fn [db _]
   (:boot-time db)))
