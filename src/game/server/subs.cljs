(ns game.server.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]
            [game.server.log.subs :as log.subs]))

(defn with-server-data-callback
  [[_ server-cid]]
  [(he/subscribed [:game|server|data server-cid])])

(def with-server-data
  #(with-server-data-callback %))

(defn server
  [db _]
  (:server db))

(rf/reg-sub
 :game|server|log
 :<- [:game|server]
 log.subs/log)

(rf/reg-sub
 :game|server|meta
 :<- [:game|server]
 (fn [db _]
   (:meta db)))

(rf/reg-sub
 :game|server|data
 :<- [:game|server]
 (fn [db [_ server-cid]]
   (get db server-cid)))

(rf/reg-sub
 :game|server|hostname
 with-server-data
 (fn [[server]]
   (println "Chamando hostname")
   (println server)
   (get-in server [:meta :hostname])))

(rf/reg-sub
 :game|server|endpoint|link
 :<- [:game|server|meta]
 (fn [meta [_ server-cid]]
   (get-in meta [:endpoints server-cid :link])))
