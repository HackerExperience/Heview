(ns game.server.notification.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]))

(defn with-notification-db-callback
  [[_ server-cid]]
  [(he/subscribed [:game|server|notification server-cid])])
(def with-notification-db
  #(with-notification-db-callback %))

(rf/reg-sub
 :game|server|notification
 he/with-game-server-data
 (fn [[server]]
   (:notification server)))

(rf/reg-sub
 :game|server|notification|all
 with-notification-db
 (fn [[db]]
   db))
