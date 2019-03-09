(ns game.server.software.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]
            [game.server.software.db :as software.db]))

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
 :game|server|software|file
 with-software-db
 (fn [[db] [_ _ file-id]]
   (software.db/search-file db file-id)))

(rf/reg-sub
 :game|server|software|files
 with-software-db
 (fn [[db]]
   (get-in db [(:main-storage-id db) :files])))

(rf/reg-sub
 :game|server|software|files|type
 (fn [[_ server-cid]]
   [(he/subscribed [:game|server|software|files server-cid])])
 (fn [[files] [_ _ file-type]]
   (reduce (fn [acc [file-id file]]
             (if (= (:type file) (name file-type))
               (assoc acc file-id file)
               acc)) {} files)))

;; TODO: Rename `cache` to `sorted`
(rf/reg-sub
 :game|server|software|files|cache
 with-software-db
 (fn [[db]]
   (get-in db [(:main-storage-id db) :cache])))

(rf/reg-sub
 :game|server|software|files|cache|type
 (fn [[_ server-cid]]
   [(he/subscribed [:game|server|software|files|cache server-cid])])
 (fn [[files] [_ _ file-type]]
   (get-in files [(name file-type) :one])))

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

(rf/reg-sub
 :game|server|software|storage|cache
 with-storage
 (fn [[db]]
   (:cache db)))
