(ns game.server.db
  (:require [game.server.log.db :as log.db]))

;; Context ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-context
  [global-db]
  (get-in global-db [:game :server]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:game :server] updated-local-db))

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

;; (defn bootstrap-account
;;   [db data]
;;   (-> db
;;       (assoc-in [:game :server :meta] (format-meta data))))

(defn bootstrap-account
  [data]
  (-> {}
      (assoc-in [:meta] (format-meta data))))

(defn server-instance
  "Creates a state instance for one specific server."
  [data]
  (-> {}
      (log.db/bootstrap-server (:logs data))))

(defn bootstrap-server
  "Adds the server data (state instance) to the global app state (db).
  Notice it creates the instance at `server-instance` independently, i.e. free
  of any kind of context. Then it wraps the instance into the global state,
  effectively adding the context (i.e. the `server-cid`).
  The wrapping is done by the caller of this method (i.e. `game.db` itself)."
  [db data server-cid]
  (assoc-in db [(name server-cid)] (server-instance data)))

;; Model ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-gateways
  [db]
  (get-in db [:meta :player]))

;; TODO (waiting backend support)
(defn get-mainframe
  [db]
  (first
   (filter #(= (:type %) :desktop) (get-gateways db))))
