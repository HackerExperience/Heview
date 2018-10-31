(ns home.login.requests
  (:require [cljs.core.match :refer-macros [match]]
            [he.core :as he]))

(defn on-login-ok
  [db response]
  (let [token (:token response)
        account-id (:account_id response)]
    {:dispatch [:setup|boot-flow token account-id]}))

(defn on-login-failed
  [db {:keys [status]}]
  (let [error-msg (match status
                         404 "User not found"
                         :else "Internal server error")]
    (assoc-in db [:home :login :form :error] error-msg)))

(defn login
  [username password]
  {:dispatch [:driver|rest|request "POST" "account/login"
              {:username username
                :password password}
              {:on-ok [:home|login|req-login-ok on-login-ok]
               :on-fail [:home|login|req-login-fail on-login-failed]}]})

