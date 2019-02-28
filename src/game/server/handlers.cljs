(ns game.server.handlers
  (:require [he.core :as he]
            [game.server.log.handlers]
            [game.server.process.handlers]
            [game.server.requests :as server.requests]))

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
