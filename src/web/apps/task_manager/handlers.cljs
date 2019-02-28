(ns web.apps.task-manager.handlers
  (:require [he.core :as he]
            [web.apps.db :as apps.db]
            [web.apps.task-manager.db :as task-manager.db]))

(he/reg-event-db
 :web|apps|task-manager|on-entry-click
 (fn [gdb [_ app-id process-id]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db
      ldb app-id #(task-manager.db/on-click % process-id) nil)
     (apps.db/set-context gdb ldb))))
