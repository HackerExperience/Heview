(ns web.apps.log-viewer.handlers
  (:require [he.core :as he]
            [web.apps.db :as apps.db]
            [web.apps.log-viewer.db :as log-viewer.db]
            [web.apps.log-viewer.popups.handlers]))

(he/reg-event-db
 :web|apps|log-viewer|on-entry-click
 (fn [gdb [_ app-id log-id]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db ldb app-id #(log-viewer.db/on-click % log-id))
     (apps.db/set-context gdb ldb))))
