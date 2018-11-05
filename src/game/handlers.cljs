(ns game.handlers
  (:require [he.core :as he]
            [setup.db]
            [game.db]
            [game.account.handlers]))


(he/reg-event-fx :game|bootstrap-account
                 (fn [{:keys [db]} [_ data]]
                   {:db (-> db
                            (game.db/bootstrap-account data)),
                    :dispatch [:game|bootstrap-account-ok]}))

(he/reg-event-fx :game|bootstrap-server
                 (fn [{:keys [db]} [_ server-id data]]
                   {:db (-> db
                            (game.db/bootstrap-server data server-id)),
                    :dispatch [:game|bootstrap-server-ok]}))

(he/reg-event-fx
 :game|bootstrap-server-ok
 (fn [{:keys [db]} _]
   (let [new-db (setup.db/dec-servers-waiting db)
         dispatch-data (if (zero? (setup.db/total-servers-waiting new-db))
                         {:dispatch [:game|bootstrap-server-ok-all]}
                         {})]
     (merge {:db new-db} dispatch-data))))


(he/reg-event-dummy :game|bootstrap-account-ok)
(he/reg-event-dummy :game|bootstrap-account-fail)
(he/reg-event-dummy :game|bootstrap-server-ok-all)
