(ns game.server.process.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]))

(defn with-process-db-callback
  [[_ server-cid]]
  [(he/subscribed [:game|server|process server-cid])])
(def with-process-db
  #(with-process-db-callback %))

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
