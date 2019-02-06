(ns web.lock.requests
  (:require [cljs.core.match :refer-macros [match]]
            [web.lock.db :as lock.db]))

(defn on-check-ok
  [_ {:keys [csrf_token]}]
  (println "check ok")
  (println csrf_token)
  {:dispatch [:boot|boot-flow csrf_token]})

(defn on-check-failed
  [db {:keys [status]}]
  (let [error-code (match status
                          403 :expired
                          :else :internal)]
    {:db (-> db
             (lock.db/passwordless-login-failed status error-code)
             (lock.db/unset-loading-status))}))


(defn check-session
  []
  {:dispatch [:driver|rest|request "GET" "check-session" :simple
              {}
              {:on-ok [:web|lock|req-check-session-ok on-check-ok]
               :on-fail [:web|lock|req-check-session-fail on-check-failed]}]})


(defn on-login-ok
  [_ {:keys [csrf_token]}]
  (println "login ok")
  (println csrf_token)
  {:dispatch [:boot|boot-flow csrf_token]})

(defn on-login-failed
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
  {:dispatch [:driver|rest|request "POST" "login" :simple
              {:username username
               :password password}
              {:on-ok [:web|lock|form|req-login-ok on-login-ok]
               :on-fail [:web|lock|form|req-login-fail on-login-failed]}]})

