(ns web.lock.requests
  (:require [cljs.core.match :refer-macros [match]]
            [driver.rest.request :as request]
            [web.lock.db :as lock.db]))

(defn on-check-ok
  [_ [_status {csrf-token :csrf_token} _]]
  {:dispatch [:boot|boot-flow csrf-token]})

(defn on-check-failed
  [db {:keys [status]}]
  (let [error-code (match status
                          403 :expired
                          :else :internal)]
    {:db (-> db
             (lock.db/passwordless-login-failed status error-code)
             (lock.db/unset-loading-status))}))


(defn check-session []
  (request/check-session
   {:on-ok [:web|lock|req-check-session-ok on-check-ok]
    :on-fail [:web|lock|req-check-session-fail on-check-failed]}))

(defn on-login-ok
  [_ [_ {csrf-token :csrf_token} _]]
  {:dispatch [:boot|boot-flow csrf-token]})

(defn on-login-fail
  [db w]
  (println "login fail")
  (println w))
  ;; (let [error-code (match status
  ;;                         404 :not_found
  ;;                         :else :internal)]
  ;;   {:db (-> db
  ;;            (lock.db/form-set-error error-code)
  ;;            (lock.db/unset-loading-status))}))

(defn login
  [username password]
  (request/account-login
   username password {:on-ok [:web|lock|form|req-login-ok on-login-ok]
                      :on-fail [:web|lock|form|req-login-fail on-login-fail]}))

