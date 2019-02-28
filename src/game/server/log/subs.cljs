(ns game.server.log.subs
  (:require [re-frame.core :as rf]
            [game.server.log.db :as log.db]
            [game.server.log.db.data :as log.db.data]))

;; Nope (not specifying `server_cid`)
(defn log
  [db _]
  (:log db))

(rf/reg-sub
 :game|server|log|entries
 :<- [:game|server]
 (fn [db [_ server-cid]]
   (get-in db [server-cid :log :entry])))

(rf/reg-sub
 :game|server|log|dropdown-map
 (fn [_ _]
   log.db.data/dropdown-map))

(rf/reg-sub
 :game|server|log|fields-data
 (fn [_ [_ log-type]]
   (log.db/get-log-fields log-type)))
