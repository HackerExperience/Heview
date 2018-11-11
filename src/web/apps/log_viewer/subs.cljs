(ns web.apps.log-viewer.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :web|apps|log-viewer|counter
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :state :current :counter])))

