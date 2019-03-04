(ns web.apps.task-manager.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]
            [web.apps.task-manager.db :as task-manager.db]))

(rf/reg-sub
 :web|apps|task-manager|entries
 (fn [[_ server-cid]]
   [(he/subscribed [:game|server|process|entries server-cid])])
 (fn [[entries]]
   (reduce task-manager.db/group-by-ip-reducer {} entries)))

(rf/reg-sub
 :web|apps|task-manager|selected
 he/with-app-state
 (fn [[db]]
   (:selected db)))
