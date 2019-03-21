(ns game.server.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]
            [game.server.log.subs :as log.subs]
            [game.server.notification.subs]
            [game.server.process.subs]
            [game.server.software.subs]))

(defn with-server-data-callback
  [[_ server-cid]]
  [(he/subscribed [:game|server|data server-cid])])
(def with-server-data
  #(with-server-data-callback %))

(defn with-server-data-meta-callback
  [[_ server-cid]]
  [(he/subscribed [:game|server|data|meta server-cid])])
(def with-server-data-meta
  #(with-server-data-meta-callback %))

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
 :game|server|meta|gateways
 :<- [:game|server|meta]
 (fn [db _]
   (:gateways db)))

(rf/reg-sub
 :game|server|meta|gateways|single-player
 :<- [:game|server|meta|gateways]
 (fn [gateways]
   (reduce (fn [acc [server-id server]]
             (if (= (:type server) "desktop_story")
               (assoc acc server-id server)
               acc)) {} gateways)))

(rf/reg-sub
 :game|server|meta|gateways|multi-player
 :<- [:game|server|meta|gateways]
 (fn [gateways]
   (reduce (fn [acc [server-id server]]
             (if (= (:type server) "desktop")
               (assoc acc server-id server)
               acc)) {} gateways)))

(rf/reg-sub
 :game|server|meta|endpoints
 :<- [:game|server|meta]
 (fn [db _]
   (:endpoints db)))

(rf/reg-sub
 :game|server|data
 :<- [:game|server]
 (fn [db [_ server-cid]]
   (get db server-cid)))

(rf/reg-sub
 :game|server|data|meta
 with-server-data
 (fn [[server]]
   (:meta server)))

(rf/reg-sub
 :game|server|data|meta|hostname
 with-server-data-meta
 (fn [[meta]]
   (:hostname meta)))

(rf/reg-sub
 :game|server|endpoint|link
 :<- [:game|server|meta]
 (fn [meta [_ server-cid]]
   (get-in meta [:endpoints server-cid :link])))
