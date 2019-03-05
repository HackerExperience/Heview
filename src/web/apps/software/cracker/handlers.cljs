(ns web.apps.software.cracker.handlers
  (:require [he.core :as he]
            [web.apps.db :as apps.db]
            [web.wm.db :as wm.db]
            [web.apps.software.cracker.db :as cracker.db]))

(he/reg-event-db
 :web|apps|software|cracker|set-tab
 (fn [gdb [_ app-id tab-id]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db
      ldb app-id #(cracker.db/on-tab-click % tab-id) nil)
     (apps.db/set-context gdb ldb))))

(he/reg-event-fx
 :web|apps|software|cracker|bruteforce
 (fn [{gdb :db} [_ app-id]]
   (let [wm-db (wm.db/get-context gdb)
         app-db (apps.db/get-context gdb)
         ip (apps.db/with-app-state app-db app-id
              #(cracker.db/bruteforce-get-ip %))

         gateway-id (wm.db/get-server-cid wm-db :local)]
     {:db gdb
      :dispatch-n (list [:game|server|software|bruteforce gateway-id ip]
                        [:web|wm|app|close app-id])})))
