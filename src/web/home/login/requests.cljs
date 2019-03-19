(ns web.home.login.requests
  (:require [cljs.core.match :refer-macros [match]]
            [driver.rest.request :as request]))

(defn on-login-ok
  [_ [_ response]]
  (println response)
  (let [setup-status (:setup_status response)
        data {:account-id (:account_id response)
              :csrf-token (:csrf_token response)}
        event (cond
                (= "ok" setup-status)
                [:boot|boot-flow :home (:csrf_token response)]
                (= "missing_signature_tos" setup-status)
                [:web|install|setup|restart :sign-tos data]
                (= "missing_signature_pp" setup-status)
                [:web|install|setup|restart :sign-pp data]
                (= "unverified" setup-status)
                [:web|install|setup|restart :verify data])]
    (println setup-status)
    (println (type setup-status))
    {:dispatch event}))

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

