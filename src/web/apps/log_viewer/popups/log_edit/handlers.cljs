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
