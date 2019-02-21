(ns web.home.login.requests
  (:require [cljs.core.match :refer-macros [match]]
            [driver.rest.request :as request]))

(defn on-login-ok
  [_ [_ {csrf-token :csrf_token}]]
  {:dispatch [:boot|boot-flow csrf-token]})

(defn on-login-fail
  [db {:keys [status]}]
  (let [error-msg (match status
                         404 "User not found"
                         400 "Application error"
                         :else "Internal server error")]
    (assoc-in db [:home :login :form :error] error-msg)))

(defn login
  [username password]
  (request/account-login
   username password {:on-ok [:web|home|login|req-login-ok on-login-ok]
                      :on-fail [:web|home|login|req-login-fail on-login-fail]}))

