(ns web.apps.log-viewer.popups.log-edit.handlers
  (:require [he.core :as he]
            [web.apps.db :as apps.db]
            [web.apps.log-viewer.popups.log-edit.db :as log-edit.db]
            [web.apps.log-viewer.popups.log-edit.validators :as v]))

(def validator (partial v/v v/state))

(he/reg-event-db
 :web|apps|log-viewer|log-edit|on-field-change
 (fn [gdb [_ app-id field-id field-type new-value]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db
      ldb
      app-id
      #(log-edit.db/on-field-change % field-id field-type new-value)
      validator)
     (apps.db/set-context gdb ldb))))

(he/reg-event-db
 :web|apps|log-viewer|log-edit|on-type-selection
 (fn [gdb [_ app-id new-type]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db
      ldb app-id #(log-edit.db/on-type-selection % new-type) validator)
     (apps.db/set-context gdb ldb))))

;; log-edit request

(he/reg-event-fx
 :web|apps|log-viewer|log-edit|log|edit
 (fn [{gdb :db} [_ app-id]]
   (let [app-db (apps.db/get-context gdb)
         state (apps.db/get-state app-db app-id)
         server-cid (log-edit.db/get-server-cid state)
         log-id (log-edit.db/get-log-id state)
         new-log (log-edit.db/get-log state)
         callback (log-edit.db/log-edit-callback app-id)]
     (println (str "https://localhost:4000/api/v1/"
                   "endpoint/1.2.3.4$,,/file/"
                   "723b,a5c,dc7a,3c13,b040,a511,cabb,4256"
                   "/upload"))
     {:db gdb
     :dispatch [:game|server|log|edit server-cid [log-id new-log] callback]
      }
     )))
