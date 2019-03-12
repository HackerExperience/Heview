(ns game.server.notification.handlers
  (:require [he.core :as he]
            [game.server.notification.db :as notification.db]))

(he/reg-event-db
 :game|server|notification|mark-all-read
 (fn [gdb [_ server-cid]]
   (as-> (notification.db/get-context gdb server-cid) ldb
     (notification.db/mark-all-read ldb)
     (notification.db/set-context gdb server-cid ldb))))
