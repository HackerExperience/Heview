(ns game.server.db
  (:require [game.server.log.db]))

;; Bootstrap ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn format-meta-player
  [data]
  (map
   (fn [x] (update x :type #(keyword %)))
   data))

(defn format-meta-remote
  [data]
  data)

(defn format-meta
  [data]
  {:player (format-meta-player (:player data))
   :remote (format-meta-remote (:remote data))})

(defn bootstrap-account
  [db data]
  (-> db
      (assoc-in [:game :server :meta] (format-meta data))))

(defn server-instance
  "Creates a state instance for one specific server."
  [data]
  (-> {}
      (game.server.log.db/bootstrap-server (:logs data))))

(defn bootstrap-server
  "Adds the server data (state instance) to the global app state (db).
  Notice it creates the instance at `server-instance` independently, i.e. free
  of any kind of context. Then it wraps the instance into the global state,
  effectively adding the context (i.e. the `server-cid`)
  "
  [db data server-cid]
    (assoc-in db [:game :server server-cid] (server-instance data)))

;; Model ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-gateways
  [db]
  (get-in db [:game :server :meta :player]))

;; TODO (waiting backend support)
(defn get-mainframe
  [db]
  (first
   (filter #(= (:type %) :desktop) (get-gateways db))))
