(ns game.handlers
  (:require [he.core :as he]
            [web.setup.db]
            [game.db]
            [game.account.handlers]))


(he/reg-event-fx :game|bootstrap|account
                 (fn [{:keys [db]} [_ data]]
                   {:db (-> db
                            (game.db/bootstrap-account data)),
                    :dispatch [:game|bootstrap|account-ok]}))

(he/reg-event-fx :game|bootstrap|servers
                 (fn [{:keys [db]} [_ servers-bootstraps]]
                   {:db (reduce game.db/bootstrap-server db servers-bootstraps)
                    :dispatch [:game|bootstrap|servers-ok]}))

(he/reg-event-dummy :game|bootstrap|account-ok)
(he/reg-event-dummy :game|bootstrap-account-fail)
(he/reg-event-dummy :game|bootstrap|servers-ok)
