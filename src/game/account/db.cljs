(ns game.account.db)

(defn account-instance
  [data]
  (:profile data))

(defn bootstrap-account
  [db data]
  (assoc-in db [:game :account] (account-instance data)))

;; Model

(defn get-username
  [db]
  (get-in db [:game :account :username]))
