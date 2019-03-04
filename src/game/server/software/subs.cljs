(ns game.server.software.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]))

(defn with-software-db-callback
  [[_ server-cid]]
  [(he/subscribed [:game|server|software server-cid])])
(def with-software-db
  #(with-software-db-callback %))

(defn with-storage-callback
  [[_ server-cid storage-id]]
  [(he/subscribed [:game|server|software|storage server-cid storage-id])])
(def with-storage
  #(with-storage-callback %))

(rf/reg-sub
 :game|server|software
 he/with-game-server-data
 (fn [[server]]
   (:software server)))

(rf/reg-sub
 :game|server|software|storage
 with-software-db
 (fn [[software] [_ _ storage-id]]
   (get software storage-id)))

(rf/reg-sub
 :game|server|software|storage-ids
 with-software-db
 (fn [[software]]
   (let [entries (into [] (keys software))]
     (he.utils/vec-remove entries (.indexOf entries :main-storage-id)))))

(rf/reg-sub
 :game|server|software|main-storage-id
 with-software-db
 (fn [[db]]
   (:main-storage-id db)))

(rf/reg-sub
 :game|server|software|storage|info
 with-storage
 (fn [[storage]]
   (:storage storage)))

(rf/reg-sub
 :game|server|software|storage|files
 with-storage
 (fn [[db]]
   (:files db)))
