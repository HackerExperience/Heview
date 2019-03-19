(ns web.install.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]
            [web.install.db :as install.db]))

(defn install
  [db]
  (:install db))

(rf/reg-sub
 :web|install|error-window
 :<- [:web|install]
 (fn [db]
   (:error-window db)))

(rf/reg-sub
 :web|install|welcome
 :<- [:web|install]
 (fn [db]
   (:welcome db)))

(rf/reg-sub
 :web|install|screen
 :<- [:web|install]
 (fn [db]
   (install.db/get-screen db)))

;; Install > Install Screen ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(rf/reg-sub
 :web|install|setup
 :<- [:web|install]
 (fn [db]
   (:setup db)))

(rf/reg-sub
 :web|install|setup|account
 :<- [:web|install|setup]
 (fn [db]
   (:account db)))

(rf/reg-sub
 :web|install|setup|step
 :<- [:web|install|setup]
 (fn [db]
   (:active-step db)))

(rf/reg-sub
 :web|install|setup|step|register
 :<- [:web|install|setup]
 (fn [db]
   (get-in db [:steps :register])))

(rf/reg-sub
 :web|install|setup|step|verify
 :<- [:web|install|setup]
 (fn [db]
   (get-in db [:steps :verify])))

(rf/reg-sub
 :web|install|setup|step|sign-tos
 :<- [:web|install|setup]
 (fn [db]
   (get-in db [:steps :sign-tos])))

(rf/reg-sub
 :web|install|setup|step|sign-pp
 :<- [:web|install|setup]
 (fn [db]
   (get-in db [:steps :sign-pp])))

(rf/reg-sub
 :web|install|setup|step|pricing
 :<- [:web|install|setup]
 (fn [db]
   (get-in db [:steps :pricing])))

(rf/reg-sub
 :web|install|setup|step|finish
 :<- [:web|install|setup]
 (fn [db]
   (get-in db [:steps :finish])))
