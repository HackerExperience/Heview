(ns web.lock.handlers
  (:require [he.core :as he]
            [web.lock.db :as lock.db]
            [web.lock.requests :as lock.requests]))

(he/reg-event-fx
 :web|lock|check-session
 (fn [{:keys [db]} _]
   (merge
    {:db (lock.db/set-loading-status db)}
    (lock.requests/check-session))))

(he/reg-event-fx
 :web|lock|req-check-session-ok
 (fn [{:keys [db]} [_ fun result xargs]]
   (merge
    {:db (lock.db/unset-loading-status db)}
    (fun db result xargs))))

(he/reg-event-fx
 :web|lock|req-check-session-fail
 (fn [{:keys [db]} [_ fun result xargs]]
    (fun db result xargs)))

(he/reg-event-db
 :web|lock|form|set-password
 (fn [db [_ password]]
   (lock.db/form-set-password db password)))

(he/reg-event-fx
 :web|lock|form|login
 (fn [{:keys [db]} _]
   (merge
    {:db (lock.db/set-loading-status db)}
    (let [username "asdf"
          password (lock.db/form-get-password db)]
      (lock.requests/login username password)))))


(he/reg-event-fx
 :web|lock|form|req-login-ok
 (fn [{:keys [db]} [_ fun result xargs]]
   (fun db result xargs)))

(he/reg-event-fx
 :web|lock|form|req-login-fail
 (fn [{:keys [db]} [_ fun result xargs]]
   (fun db result xargs)))
