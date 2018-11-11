(ns web.wm.subs
  (:require [re-frame.core :as rf]))

(defn wm
  [db _]
  (:wm db))

(rf/reg-sub
 :web|wm|apps
 :<- [:web|wm]
 (fn [db _]
   (:apps db)))

(rf/reg-sub
 :web|wm|active-session
 :<- [:web|wm]
 (fn [db _]
   (:active-session db)))

(rf/reg-sub
 :web|wm|session
 :<- [:web|wm]
 (fn [db _]
   (:sessions db)))

(rf/reg-sub
 :web|wm|session|apps
 :<- [:web|wm|session]
 (fn [db [_ session-id]]
   (get-in db [session-id :apps])))

(rf/reg-sub
 :web|wm|apps|window-data
 :<- [:web|wm|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :window])))

(rf/reg-sub
 :web|wm|apps|context
 :<- [:web|wm|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :meta :context])))

(rf/reg-sub
 :web|wm|apps|state
 :<- [:web|wm|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :state :current])))

