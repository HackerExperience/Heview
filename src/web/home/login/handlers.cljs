(ns web.home.login.handlers
  (:require [he.core :as he]
            [web.home.login.requests :as requests]))

(he/reg-event-db :home|login|form|change-username
                 (fn [db [_ v]]
                   (assoc-in db [:home :login :form :username] v)))

(he/reg-event-db :home|login|form|change-password
                 (fn [db [_ v]]
                   (assoc-in db [:home :login :form :password] v)))

(he/reg-event-fx :home|login|login
                 [(he/inject-sub [:home|login|form|username])
                  (he/inject-sub [:home|login|form|password])]
                 (fn [cofx _]
                   (let [{username :home|login|form|username
                          password :home|login|form|password} cofx]
                     (requests/login username password))))

(he/reg-event-db :home|login|req-login-fail
                 (fn [db [_ [fun] result]]
                   (fun db result)))

(he/reg-event-fx :home|login|req-login-ok
                 (fn [db [_ [fun] result]]
                   (fun db result)))
