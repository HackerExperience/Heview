(ns game.server.notification.db
  (:require [he.utils]
            [game.notification.db]))

;; Context

(defn get-context
  [global-db server-cid]
  (get-in global-db [:game :server server-cid :notification]))

(defn set-context
  [gdb server-cid updated-local-db]
  (assoc-in gdb [:game :server server-cid :notification] updated-local-db))

;; Model

(defn mark-all-read
  [db]
  (reduce (fn [acc [entry-id entry]]
            (assoc acc entry-id (assoc entry :is-read? true))) {} db))

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

(defn bootstrap-server
  [db data]
  (-> db
      (assoc :notification (reduce bootstrap-reducer {} data))))
