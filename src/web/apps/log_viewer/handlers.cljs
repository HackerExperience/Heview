(ns web.apps.log-viewer.handlers
  (:require [he.core :as he]
            [web.apps.db :as apps.db]
            [web.apps.log-viewer.db :as log-viewer.db]))

(he/reg-event-db
 :web|apps|log-viewer|inc
 (fn [db [_ app-id]]
   (apps.db/update-db db app-id #(log-viewer.db/counter-inc %))))

(he/reg-event-db
 :web|apps|log-viewer|dec
 (fn [db [_ app-id]]
   (apps.db/update-db db app-id #(log-viewer.db/counter-dec %))))
