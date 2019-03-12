(ns game.notification.requests
  (:require [driver.rest.request :as request]))

(defn mark-all-read
  [class id]
  (request/notification-read-all
   {:class class :class_id id}
   {:on-ok [:dev|null] :on-fail [:dev|null]}))
