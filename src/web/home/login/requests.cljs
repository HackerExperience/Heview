(ns web.home.login.requests
  (:require [cljs.core.match :refer-macros [match]]))

(defn on-login-ok
  [_ {:keys [csrf_token]}]
  {:dispatch [:boot|boot-flow csrf_token]})

(defn on-login-failed
  [db {:keys [status]}]
  (let [error-msg (match status
                         404 "User not found"
                         400 "Application error"
                         :else "Internal server error")]
    (assoc-in db [:home :login :form :error] error-msg)))

(defn login
  [username password]
  {:dispatch [:driver|rest|request "POST" "login" :simple
              {:username username
               :password password}
              {:on-ok [:web|home|login|req-login-ok on-login-ok]
               :on-fail [:web|home|login|req-login-fail on-login-failed]}]})

