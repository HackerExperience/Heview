(ns web.wm.handlers
  (:require [cljs.core.match :refer-macros [match]]
            [he.core :as he]
            [game.server.db :as server.db]
            [web.wm.db :as wm.db]))

;; Bootstrap ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-fx
 :web|bootstrap
 (fn [{:keys [db]} _]
   (let [gateways (server.db/get-gateways db)
         mainframe (server.db/get-mainframe db)]
     {:db (wm.db/bootstrap db gateways mainframe)
      :dispatch [:web|bootstrap-ok]})))

(he/reg-event-dummy :web|bootstrap-ok)

;; App ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-fx
 :web|wm|app|launch
 (fn [{:keys [db]} [_ app-type]]
   (match (wm.db/open db app-type)
          [:ok new-db] {:db new-db}
          [:error reason] {:db db
                           :dispatch [:web|wm|app|open-failed reason]})))

(he/reg-event-fx
 :web|wm|app|close
 (fn [{:keys [db]} [_ app-id]]
   (match (wm.db/close db app-id)
          [:ok new-db] {:db new-db}
          [:error reason] {:db db
                           :dispatch [:web|wm|app|close-failed reason]})))
