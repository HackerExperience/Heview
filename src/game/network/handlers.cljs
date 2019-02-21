(ns game.network.handlers
  (:require [he.core :as he]
            [game.network.requests :as network.requests]))

(he/reg-event-fx
 :game|network|browse
 (fn [{gdb :db} [_ server-cid ip callback]]
   "Calls the `browse` endpoint with either an IP address or an in-game URL."
   (network.requests/browse server-cid ip callback)))

(defn deny-request-callback
  ([callback-data reason]
   (deny-request-callback callback-data reason 600))
  ([{[callback & xargs] :on-fail} reason status]
   {:dispatch [callback status reason {} (into [] xargs)]}))

(he/reg-event-fx
 :game|network|browse-ip
 (fn [{gdb :db} [_ server-cid ip callback]]
   "Calls the `browse` endpoint, but only accepts an IP address as input."
   (if (he.utils/valid-ipv4? ip)
     (network.requests/browse server-cid ip callback)
     (deny-request-callback callback :invalid-ip))))

(he/reg-event-fx :game|network|req-browse-ok
                 (fn [{gdb :db} [_ fun result xargs]]
                   (fun gdb result xargs)))
(he/reg-event-fx :game|network|req-browse-fail
                 (fn [{gdb :db} [_ fun result xargs]]
                   (fun gdb result xargs)))
