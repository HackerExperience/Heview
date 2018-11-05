(ns driver.ws.handlers
  (:require [he.core :as he]
            [web.setup.db]
            [driver.ws.db]))

(he/reg-event-fx :driver|ws|connect
                 (fn [{:keys [db]} [_ token]]
                   {:db (-> db
                            (driver.ws.db/connect token))
                    :dispatch [:driver|ws|connect-ok]}))

(he/reg-event-db :driver|ws|join-account
                 (fn [db [_ account-id]]
                   (driver.ws.db/join-account db account-id)
                   db))

(he/reg-event-fx :driver|ws|join-servers
                 [(he/inject-sub [:game|server|joinable])]
                 (fn [cofx _]
                   (let [{joinable :game|server|joinable
                          db :db} cofx]
                     (doseq [server-id joinable]
                       (driver.ws.db/join-server db server-id))
                     {:db (web.setup.db/wait-for-servers db (count joinable))})))

(he/reg-event-dummy :driver|ws|connect-ok)

(he/reg-event-db :driver|ws|teardown
                 (fn [db _]
                   (driver.ws.db/teardown db)))
