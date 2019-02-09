(ns web.apps.log-viewer.subs
  (:require [re-frame.core :as rf]
            [web.apps.log-viewer.db :as log-viewer.db]
            [web.apps.log-viewer.popups.subs]))

(rf/reg-sub
 :web|apps|log-viewer|counter
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :state :current :counter])))

(rf/reg-sub
 :web|apps|log-viewer|entries
 (fn [[_ server-cid] _]
   [(rf/subscribe [:game|server|log|entries server-cid])])
 (fn [[entries] _]
   (map log-viewer.db/format-log entries)))

(rf/reg-sub
 :web|apps|log-viewer|selected-entry
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :state :current :selected])))

