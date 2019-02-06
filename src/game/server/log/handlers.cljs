(ns game.server.log.handlers
  (:require [he.core :as he]
            [game.server.log.db :as log.db]))

(defn get-db-context
  [global-db server-cid]
  (get-in global-db [:game :server server-cid :log]))

(defn set-db-context
  [global-db updated-local-db server-cid]
  (assoc-in global-db [:game :server server-cid :log] updated-local-db))

(he/reg-event-db :game|server|log|on-log-created
                 (fn [gdb [_ server-cid log]]
                   (as-> (get-db-context gdb server-cid) ldb
                     (log.db/add-new-log ldb log)
                     (set-db-context gdb ldb server-cid))))
