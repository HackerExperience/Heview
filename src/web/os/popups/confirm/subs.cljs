(ns web.os.popups.confirm.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :web|os|popups|confirm|config
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :state :current :config])))
