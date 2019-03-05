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
  [gdb app-id [file-type file-id]]
  (println file-type)
  (println (name file-type))
  (let [software-type (keyword (str "software-" (name file-type)))]
    (println software-type)
    {:dispatch [:web|wm|app|open software-type {:file-id file-id}]}))

(defn handle-action-download
  [gdb app-id [_ file-id]]
  (let [wm-db (wm.db/get-context gdb)
        server-cid (wm.db/get-server-cid wm-db :remote)]
    {:dispatch [:game|server|software|download server-cid file-id {}]}))

(defn handle-action-upload
  [gdb app-id [_ file-id]]
  (let [wm-db (wm.db/get-context gdb)
        server-cid (wm.db/get-server-cid wm-db :remote)]
    (when (nil? server-cid)
      (he.error/runtime "Attempt to upload but there is no remote!"))
    {:dispatch [:game|server|software|upload server-cid file-id {}]}))

(defn handle-action-delete
  [gdb app-id [_ file-id]]
  {:db gdb})

(he/reg-event-fx
 :web|apps|file-explorer|file-action
 (fn [{gdb :db} [_ app-id file-info action-id]]
   (match action-id
          :execute (handle-action-execute gdb app-id file-info)
          :download (handle-action-download gdb app-id file-info)
          :upload (handle-action-upload gdb app-id file-info)
          :delete (handle-action-delete gdb app-id file-info)
          else (he.error/match "File action" else))))
