(ns web.apps.subs
  (:require [re-frame.core :as rf]
            [web.apps.log-viewer.subs]))

(defn apps
  [db _]
  (get-in db [:apps]))

(rf/reg-sub
 :web|apps|state
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :state :current])))

(rf/reg-sub
 :web|apps|context
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :meta :context])))

(rf/reg-sub
 :web|apps|type
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :meta :type])))


