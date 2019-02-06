(ns web.lock.db)

(defn set-loading-status
  [db]
  (assoc-in db [:web :lock :loading?] true))

(defn unset-loading-status
  [db]
  (assoc-in db [:web :lock :loading?] false))

(defn passwordless-login-failed
  [db status error-code]
  (-> db
      (assoc-in [:web :lock :fail-reason] error-code)))

(defn form-set-password
  [db password]
  (assoc-in db [:web :lock :form :password] password))

(defn form-get-password
  [db]
  (get-in db [:web :lock :form :password]))

(defn form-set-error
  [db error-code]
  (assoc-in db [:web :lock :form :error] error-code))
