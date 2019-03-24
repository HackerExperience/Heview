(ns game.story.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]))

(defn story
  [db _]
  (:story db))

(defn with-contact-callback
  [[_ contact-id]]
  [(he/subscribed [:game|story|contact contact-id])])
(def with-contact
  #(with-contact-callback %))

(rf/reg-sub
 :game|story|contact
 :<- [:game|story]
 (fn [db [_ contact-id]]
   (get-in db [:contact contact-id])))

(rf/reg-sub
 :game|story|contact|emails
 with-contact
 (fn [[contact]]
   (:emails contact)))

(rf/reg-sub
 :game|story|contact|replies
 with-contact
 (fn [[contact]]
   (:replies contact)))
