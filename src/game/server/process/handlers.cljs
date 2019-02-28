(ns game.server.process.handlers
  (:require [he.core :as he]
            [game.server.process.db :as process.db]))

(he/reg-event-db
 :game|server|process|on-top-recalcado
 (fn [gdb [_ server-cid processes]]
   (as-> (process.db/get-context gdb server-cid) ldb
     (process.db/on-top-recalcado ldb processes)
     (process.db/set-context gdb server-cid ldb))))
