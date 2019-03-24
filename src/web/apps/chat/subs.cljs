(ns web.apps.chat.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]
            [web.apps.chat.db :as chat.db]))

(rf/reg-sub
 :web|apps|chat|type
 he/with-app-state
 (fn [[db]]
   (:type db)))

(rf/reg-sub
 :web|apps|chat|contact-id
 he/with-app-state
 (fn [[db]]
   (:contact-id db)))

(rf/reg-sub
 :web|apps|chat|loading?
 he/with-app-state
 (fn [[db]]
   (:loading? db)))

(rf/reg-sub
 :web|apps|chat|story|entries
 (fn [[_ contact-id]]
   (println "Contact id " contact-id)
   [(he/subscribed [:game|story|contact contact-id])
    (he/subscribed [:game|story|contact|emails contact-id])])
 (fn [[contact emails]]
   (let [player {:id "player-id"
                 :name "Nerdola"
                 :avatar "NotYet"}]
     (chat.db/format-entries-story player contact emails))))

(rf/reg-sub
 :web|apps|chat|story|replies
 (fn [[_ contact-id]]
   (println "Wtf " contact-id)
   [(he/subscribed [:game|story|contact|replies contact-id])])
 (fn [[replies]]
   replies))
