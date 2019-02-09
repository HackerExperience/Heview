(ns web.os.popups.os-error.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :web|os|popups|os-error|reason
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :state :current :reason])))

(rf/reg-sub
 :web|os|popups|os-error|source
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :state :current :source])))
