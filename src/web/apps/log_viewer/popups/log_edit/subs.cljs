(ns web.apps.log-viewer.popups.log-edit.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :web|apps|log-viewer|log-edit
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :state :current])))

(rf/reg-sub
 :web|apps|log-viewer|log-edit|log
 (fn [[_ app-id] _]
   [(rf/subscribe [:web|apps|log-viewer|log-edit app-id])])
 (fn [[state] _]
   (:log state)))

(rf/reg-sub
 :web|apps|log-viewer|log-edit|log|html
 (fn [[_ app-id] _]
   [(rf/subscribe [:web|apps|log-viewer|log-edit|log app-id])])
 (fn [[log] _]
   (get-in log [:value :html])))

(rf/reg-sub
 :web|apps|log-viewer|log-edit|log|type
 (fn [[_ app-id] _]
   [(rf/subscribe [:web|apps|log-viewer|log-edit|log app-id])])
 (fn [[log] _]
   (:type log)))

(rf/reg-sub
 :web|apps|log-viewer|log-edit|log|data
 (fn [[_ app-id] _]
   [(rf/subscribe [:web|apps|log-viewer|log-edit|log app-id])])
 (fn [[log] _]
   (:data log)))

(rf/reg-sub
 :web|apps|log-viewer|log-edit|invalid-fields
 (fn [[_ app-id] _]
   [(rf/subscribe [:web|apps|log-viewer|log-edit app-id])])
 (fn [[state] _]
   (:invalid-fields state)))

(rf/reg-sub
 :web|apps|log-viewer|log-edit|field-valid?
 (fn [[_ app-id field-id] _]
   [(rf/subscribe [:web|apps|log-viewer|log-edit|invalid-fields app-id])
    field-id])
 (fn [[invalid-fields] [_ _ field-id]]
   (if (some #(= field-id %) invalid-fields)
     false
     true)))

(rf/reg-sub
 :web|apps|log-viewer|log-edit|corrupt?
 (fn [[_ app-id] _]
   [(rf/subscribe [:web|apps|log-viewer|log-edit|invalid-fields app-id])])
 (fn [[invalid-fields] _]
   (if (empty? invalid-fields)
     false
     true)))

