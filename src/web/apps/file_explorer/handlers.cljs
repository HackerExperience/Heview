(ns web.apps.file-explorer.handlers
  (:require [cljs.core.match :refer-macros [match]]
            [he.core :as he]
            [web.apps.db :as apps.db]
            [web.wm.db :as wm.db]
            [web.apps.file-explorer.db :as file-explorer.db]))

(he/reg-event-db
 :web|apps|file-explorer|file-click
 (fn [gdb [_ app-id file-id]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db
      ldb app-id #(file-explorer.db/on-click-file % file-id) nil)
     (apps.db/set-context gdb ldb))))

(he/reg-event-db
 :web|apps|file-explorer|sort-click
 (fn [gdb [_ app-id sort-id]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db
      ldb app-id #(file-explorer.db/on-click-sort % sort-id) nil)
     (apps.db/set-context gdb ldb))))

(defn handle-action-execute
  [gdb app-id file-id]
  {:db gdb})

(defn handle-action-download
  [gdb app-id file-id]
  (let [wm-db (wm.db/get-context gdb)
        server-cid (wm.db/get-server-cid wm-db :remote)]
    {:dispatch [:game|server|software|download server-cid file-id {}]}))

(defn handle-action-upload
  [gdb app-id file-id]
  (let [wm-db (wm.db/get-context gdb)
        server-cid (wm.db/get-server-cid wm-db :remote)]
    (when (nil? server-cid)
      (he.error/runtime "Attempt to upload but there is no remote!"))
    {:dispatch [:game|server|software|upload server-cid file-id {}]}))

(defn handle-action-delete
  [gdb app-id file-id]
  {:db gdb})

(he/reg-event-fx
 :web|apps|file-explorer|file-action
 (fn [{gdb :db} [_ app-id file-id action-id]]
   (match action-id
          :execute (handle-action-execute gdb app-id file-id)
          :download (handle-action-download gdb app-id file-id)
          :upload (handle-action-upload gdb app-id file-id)
          :delete (handle-action-delete gdb app-id file-id)
          else (he.error/match "File action" else))))
