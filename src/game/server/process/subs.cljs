(ns game.server.process.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]))

(defn with-process-db-callback
  [[_ server-cid]]
  [(he/subscribed [:game|server|process server-cid])])
(def with-process-db
  #(with-process-db-callback %))

(defn with-process-entries-callback
  [[_ server-cid]]
  [(he/subscribed [:game|server|process|entries server-cid])])
(def with-process-entries
  #(with-process-entries-callback %))

(rf/reg-sub
 :game|server|process
 he/with-game-server-data
 (fn [[server]]
   (:process server)))

(rf/reg-sub
 :game|server|process|entries
 with-process-db
 (fn [[db]]
   (:entries db)))

(rf/reg-sub
 :game|server|process|entries|timed
 with-process-entries
 (fn [[entries]]
   (filter (fn [[_ x]] (not (nil? (:completion-date (:progress x))))) entries)))

(rf/reg-sub
 :game|server|process|entries|untimed
 with-process-entries
 (fn [[entries]]
   (filter (fn [[_ x]] (nil? (:completion-date (:progress x)))) entries)))
