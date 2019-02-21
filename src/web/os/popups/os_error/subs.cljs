(ns web.os.popups.os-error.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]))

(rf/reg-sub
 :web|os|popups|os-error|reason
 he/with-app-state
 (fn [[state] _]
   (:reason state)))

(rf/reg-sub
 :web|os|popups|os-error|source
 he/with-app-state
 (fn [[state] _]
   (:source state)))
