(ns web.os.handlers
  (:require [he.core :as he]
            [web.os.db :as os.db]
            [web.os.popups.handlers]))

(he/reg-event-fx
 :web|os|error|runtime
 (fn [{gdb :db} [_ error-data]]
   (let [ldb (os.db/get-context gdb)
         should-open-popup? (not (os.db/has-os-error? ldb error-data))
         new-ldb (if should-open-popup?
                   (os.db/add-os-error ldb error-data)
                   ldb)
         dispatch-event (when should-open-popup?
                          [:web|wm|app|open-popup
                           [:os :os-error nil] [error-data] []])]
     {:db (os.db/set-context gdb new-ldb)
      :dispatch-n (list dispatch-event)})))

(he/reg-event-db
 :web|os|error|runtime-close
 (fn [gdb [_ reason]]
   (as-> (os.db/get-context gdb) ldb
     (os.db/remove-os-error ldb reason)
     (os.db/set-context gdb ldb))))

(he/reg-event-fx
 :web|os|bootstrap
 (fn [{gdb :db} _]
   {:db (as-> {} ldb
          (os.db/bootstrap ldb)
          (os.db/set-context gdb ldb))}))
