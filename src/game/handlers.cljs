(ns game.handlers
  (:require [he.core :as he]
            [game.db]
            [game.requests]
            [game.account.handlers]
            [game.notification.handlers]
            [game.server.handlers]
            [game.network.handlers]))

;; Bootstrap ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-fx :game|bootstrap|account
                 (fn [{:keys [db]} [_ account-bootstrap]]
                   {:db (-> db
                            (game.db/bootstrap-account account-bootstrap))
                    :dispatch [:game|bootstrap|account-ok]}))

(he/reg-event-fx :game|bootstrap|servers
                 (fn [{:keys [db]} [_ bootstrap]]
                   (println "Handler bs server")
                   ;; (cljs.pprint/pprint db)
                   {:db (game.db/bootstrap-server db bootstrap)
                    ;(reduce game.db/bootstrap-server db servers-bootstraps)
                    :dispatch [:game|bootstrap|servers-ok]}))

(he/reg-event-dummy :game|bootstrap|account-ok)
(he/reg-event-dummy :game|bootstrap-account-fail)
(he/reg-event-dummy :game|bootstrap|servers-ok)

;; Game

(he/reg-event-fx :game|logout
                 (fn [_ _]
                   (game.requests/logout)))

(he/reg-event-fx :game|req-logout-ok
                 (fn [{:keys [db]} [_ fun result xargs]]
                   (fun db result xargs)))

(he/reg-event-fx :game|req-logout-fail
                 (fn [{:keys [db]} [_ fun result xargs]]
                   (fun db result xargs)))

;; Misc

(he/reg-event-fx
 :game|post-boot-hook
 (fn [{gdb :db} _]
   {:db gdb}))
   ;; {:db (assoc gdb :tick (int (/ (.now js/Date) 1000)))
   ;;  :dispatch-n (list [:game|tick])}))

;; (he/reg-event-fx
;;  :game|tick
;;  (fn [{gdb :db} _]
;;    {:db (update gdb :tick inc)
;;     :dispatch-later [{:ms 1000 :dispatch [:game|tick]}]}))

(he/reg-event-fx :dev|null
                 (fn [{gdb :db} _]
                   {:db gdb}))
