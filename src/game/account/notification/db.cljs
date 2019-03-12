(ns game.account.notification.db
  (:require [he.utils]
            [game.notification.db]))

;; Context

(defn get-context
  [global-db]
  (get-in global-db [:game :account :notification]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:game :account :notification] updated-local-db))

;; Bootstrap

(defn bootstrap-reducer
  [acc raw-notification]
  (let [notif-id (he.utils/get-canonical-id (:notification_id raw-notification))
        notification (game.notification.db/build-notification raw-notification)
        client-meta (game.notification.db/build-meta notification)]
    (into
     (sorted-map-by >)
     (assoc acc notif-id (merge notification
                                {:client-meta client-meta})))))

(defn bootstrap-account
  [db data]
  (-> db
      (assoc :notification (reduce bootstrap-reducer {} data))))
