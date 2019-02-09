(ns web.os.db)

;; Context

(defn get-context
  [global-db]
  (get-in global-db [:web :os]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:web :os] updated-local-db))

;; Model

(defn throw-runtime-error
  [db reason]
  (-> db
      (assoc-in [:runtime-error] reason)))
