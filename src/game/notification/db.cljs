(ns game.notification.db
  (:require [game.notification.db.type :as notification.db.type]))

(defn get-static-meta
  [notification]
  {})

(defn build-meta
  [notification]
  (let [dynamic-meta (notification.db.type/get-dynamic-meta notification)
        static-meta (get-static-meta notification)]
    (merge dynamic-meta
           static-meta)))

(defn build-notification
  [raw-notification]
  (let [code (:code raw-notification)]
    {:code code
     :creation-time (:creation_time raw-notification)
     :data (notification.db.type/get-data code (:data raw-notification))
     :is-read? (:is_read raw-notification)}))
