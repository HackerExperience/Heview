(ns game.handlers
  (:require [he.core :as he]
            [game.db]
            [game.requests]
            [game.account.handlers]
            [game.server.handlers]))

;; Bootstrap ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-fx :game|bootstrap|account
                 (fn [{:keys [db]} [_ data]]
                   {:db (-> db
                            (game.db/bootstrap-account data))
                    :dispatch [:game|bootstrap|account-ok]}))

(he/reg-event-fx :game|bootstrap|servers
                 (fn [{:keys [db]} [_ servers-bootstraps]]
                   {:db (reduce game.db/bootstrap-server db servers-bootstraps)
                    :dispatch [:game|bootstrap|servers-ok]}))

(he/reg-event-dummy :game|bootstrap|account-ok)
(he/reg-event-dummy :game|bootstrap-account-fail)
(he/reg-event-dummy :game|bootstrap|servers-ok)

;; Game

(he/reg-event-fx :game|logout
                 (fn [_ _]
                   (game.requests/logout)))

(he/reg-event-fx :game|req-logout-ok
                 (fn [{:keys [db]} [_ [fun] result]]
                   (fun db result)))

(he/reg-event-fx :game|req-logout-fail
                 (fn [{:keys [db]} [_ [fun] result]]
                   (fun db result)))

