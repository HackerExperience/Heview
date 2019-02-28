(ns game.server.log.handlers
  (:require [he.core :as he]
            [game.server.log.db :as log.db]
            [game.server.log.requests :as log.requests]))

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

(he/reg-event-fx
 :game|server|log|edit
 (fn [{gdb :db} [_ server-cid log callback]]
   (log.requests/forge-edit server-cid log callback)))

(he/reg-event-fx
 :game|server|log|req-edit-ok
 (fn [{gdb :db} [_ fun result xargs]]
   (fun gdb result xargs)))
(he/reg-event-fx
 :game|server|log|req-edit-fail
 (fn [{gdb :db} [_ fun result xargs]]
   (fun gdb result xargs)))
