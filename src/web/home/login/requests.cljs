(ns web.home.login.requests
  (:require [cljs.core.match :refer-macros [match]]
            [he.core :as he]))

(defn on-login-ok
  [db response]
  (let [session-id (:session_id response)
        account-id (:account_id response)
        bootstrap (:bootstrap response)]
    {:dispatch [:boot|flow session-id account-id bootstrap]}))

(defn on-login-failed
  [db {:keys [status]}]
  (let [error-msg (match status
                         404 "User not found"
                         400 "Application error"
                         :else "Internal server error")]
    (assoc-in db [:home :login :form :error] error-msg)))

(defn login
  [username password]
  {:dispatch [:driver|rest|request "POST" "login"
              {:username username
               :password password
               :client "web1"}
              {:on-ok [:web|home|login|req-login-ok on-login-ok]
               :on-fail [:web|home|login|req-login-fail on-login-failed]}]})

