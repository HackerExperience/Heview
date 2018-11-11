(ns web.wm.subs
  (:require [re-frame.core :as rf]))

(defn wm
  [db _]
  (:wm db))

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
 :web|wm|windows
 :<- [:web|wm]
 (fn [db _]
   (:windows db)))

(rf/reg-sub
 :web|wm|window-data
 :<- [:web|wm|windows]
 (fn [db [_ app-id]]
   (get db app-id)))
