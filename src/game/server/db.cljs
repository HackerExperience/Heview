(ns game.server.db
  (:require [game.server.log.db]))

(defn bootstrap-account
  [db data]
  (-> db
      (assoc-in [:game :server :meta] data)))

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
