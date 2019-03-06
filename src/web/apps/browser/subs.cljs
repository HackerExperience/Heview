(ns web.apps.browser.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]
            [web.apps.browser.page.subs]))

(defn with-current-tab-callback
  [[_ app-id]]
  [(he/subscribed [:web|apps|browser|tab app-id])])
(def with-current-tab
  #(with-current-tab-callback %))

(defn with-current-tab-root-callback
  [[_ app-id]]
  [(he/subscribed [:web|apps|browser|tab|root app-id])])
(def with-current-tab-root
  #(with-current-tab-root-callback %))

(rf/reg-sub
 :web|apps|browser|tabs
 he/with-app-state
 (fn [[db]]
   (:tabs db)))

(rf/reg-sub
 :web|apps|browser|tab
 he/with-app-state
 (fn [[db]]
   (:active-tab db)))

(rf/reg-sub
 :web|apps|browser|tab|id
 he/with-app-state
 (fn [[db]]
   (:active-tab-id db)))

(rf/reg-sub
 :web|apps|browser|tab|input-url
 with-current-tab
 (fn [[tab]]
   (:input-url tab)))

(rf/reg-sub
 :web|apps|browser|tab|base-url
 with-current-tab
 (fn [[tab]]
   (:base-url tab)))

(rf/reg-sub
 :web|apps|browser|tab|page
 with-current-tab
 (fn [[tab]]
   (:page tab)))

(rf/reg-sub
 :web|apps|browser|tab|links
 with-current-tab
 (fn [[tab]]
   (:links tab)))

(rf/reg-sub
 :web|apps|browser|tab|path
 with-current-tab
 (fn [[tab]]
   (:path tab)))

(rf/reg-sub
 :web|apps|browser|tab|custom
 with-current-tab
 (fn [[tab]]
   (:custom tab)))

(rf/reg-sub
 :web|apps|browser|tab|root
 he/with-app-state
 (fn [[db]]
   (nth (:tabs db) (:active-tab-id db))))

(rf/reg-sub
 :web|apps|browser|tab|next?
 with-current-tab-root
 (fn [[tab]]
   (not (empty? (:next tab)))))

(rf/reg-sub
 :web|apps|browser|tab|previous?
 with-current-tab-root
 (fn [[tab]]
   (not (empty? (:previous tab)))))
