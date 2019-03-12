(ns game.account.db
  (:require [game.account.notification.db :as notification.db]))

;; Context

(defn get-context
  [global-db]
  (get-in global-db [:game :account]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:game :account] updated-local-db))

;; Bootstrap

(defn initial-instance
  [data]
  {:profile data
   :username "todo"})

(defn account-instance
  [data]
  (-> (initial-instance data)
      (notification.db/bootstrap-account (:notifications data))))

(defn bootstrap-account
  [data]
  (account-instance data))

;; Model

(defn get-username
  [db]
  (get-in db [:game :account :username]))
