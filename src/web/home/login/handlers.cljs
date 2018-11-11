(ns web.home.login.handlers
  (:require [he.core :as he]
            [web.home.login.requests :as requests]))

(he/reg-event-db :web|home|login|form|change-username
                 (fn [db [_ v]]
                   (assoc-in db [:home :login :form :username] v)))

(he/reg-event-db :web|home|login|form|change-password
                 (fn [db [_ v]]
                   (assoc-in db [:home :login :form :password] v)))

(he/reg-event-fx :web|home|login|login
                 [(he/inject-sub [:web|home|login|form|username])
                  (he/inject-sub [:web|home|login|form|password])]
                 (fn [cofx _]
                   (let [{username :web|home|login|form|username
                          password :web|home|login|form|password} cofx]
                     (requests/login username password))))

(he/reg-event-db :web|home|login|req-login-fail
                 (fn [db [_ [fun] result]]
                   (fun db result)))

(he/reg-event-fx :web|home|login|req-login-ok
                 (fn [db [_ [fun] result]]
                   (fun db result)))
