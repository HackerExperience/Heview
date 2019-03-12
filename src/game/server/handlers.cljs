(ns game.server.handlers
  (:require [he.core :as he]
            [game.server.log.handlers]
            [game.server.notification.handlers]
            [game.server.process.handlers]
            [game.server.software.handlers]
            [game.server.requests :as server.requests]))

;; Helix Event handlers

(he/reg-event-fx
 :game|server|password-acquired
 (fn [_ [_ data]]
   {:dispatch [:web|apps|remote-access|on-password-acquired data]}))

;; Requests

(he/reg-event-fx
 :game|server|login
 (fn [{gdb :db} [_ server-cid ip pass callback]]
   (server.requests/login server-cid ip pass callback)))

(he/reg-event-fx :game|server|req-login-ok
                 (fn [{gdb :db} [_ fun result xargs]]
                   (fun gdb result xargs)))
(he/reg-event-fx :game|server|req-login-fail
                 (fn [{gdb :db} [_ fun result xargs]]
                   (fun gdb result xargs)))
