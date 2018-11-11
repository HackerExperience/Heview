(ns web.apps.log-viewer.handlers
  (:require [he.core :as he]
            [web.wm.db :as wm.db]
            [web.apps.log-viewer.db :as log-viewer.db]))

(he/reg-event-db
 :web|apps|log-viewer|inc
 (fn [db [_ app-id]]
   (wm.db/update-app-db db app-id #(log-viewer.db/counter-inc %))))

(he/reg-event-db
 :web|apps|log-viewer|dec
 (fn [db [_ app-id]]
   (wm.db/update-app-db db app-id #(log-viewer.db/counter-dec %))))
