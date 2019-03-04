(ns game.server.log.db
  (:require [he.date]
            [he.utils]
            [game.server.log.db.data :as log.db.data]))

;; Context

(defn get-context
  [global-db server-cid]
  (get-in global-db [:game :server server-cid :log]))
(defn get-context-game
  [game-db server-cid]
  (get-in game-db [:server server-cid :log]))

(defn set-context
  [global-db server-cid updated-local-db]
  (assoc-in global-db [:game :server server-cid :log] updated-local-db))

;; Model

(defn get-log-fields
  [log-type]
  (get log.db.data/log-map log-type))

(defn get-log
  [db log-id]
  (get-in db [:entry log-id]))

(defn format-log
  [log]
  (let [{timestamp :timestamp
         type :type
         data :data} log]
    {:datetime (he.date/timestamp-to-datetime timestamp)
     :type type
     :data data
     :value (log.db.data/derive-log-text log)}))

(defn add-new-log
  [db log]
  (let [log-id (he.utils/get-canonical-id (:log_id log))
        new-logs (conj (:entry db) {log-id (format-log log)})]
    (-> db
        (assoc-in [:entry] new-logs))))

;; Bootstrap

(defn bootstrap-log-reducer
  "Reduces the raw bootstrap response into the proper internal format.
  Removes the `log_id` information, since it's already present at the map key."
  [acc log]
  (let [log-id (he.utils/get-canonical-id (:log_id log))]
    (into
     (sorted-map-by >)
     (assoc acc log-id (format-log log)))))

(defn bootstrap-server
  [db data]
  (-> db
      (assoc-in [:log :entry] (reduce bootstrap-log-reducer {} data))))
