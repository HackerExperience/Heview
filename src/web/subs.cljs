(ns web.subs
  (:require [re-frame.core :as rf]
            [web.home.subs :as home.subs]
            [web.lock.subs :as lock.subs]
            [web.wm.subs :as wm.subs]
            [web.apps.subs :as apps.subs]
            [web.os.subs :as os.subs]))

(defn web
  [db _]
  (:web db))

(rf/reg-sub
 :web|home
 home.subs/home)

(rf/reg-sub
 :web|wm
 :<- [:web]
 wm.subs/wm)

(rf/reg-sub
 :web|apps
 :<- [:web]
 apps.subs/apps)

(rf/reg-sub
 :web|lock
 :<- [:web]
 lock.subs/lock)

(rf/reg-sub
 :web|meta
 :<- [:web]
 (fn [db _]
  (:meta db)))

;; TODO: Check according to expiration date
(rf/reg-sub
 :web|meta|cookie-exists?
 :<- [:web|meta]
 (fn [db _] 
   (some? (:username db))))

(rf/reg-sub
 :web|meta|username
 :<- [:web|meta]
 (fn [db _]
   (:username db)))
