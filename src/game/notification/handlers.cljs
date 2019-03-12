(ns game.notification.handlers
  (:require [he.core :as he]
            [cljs.core.match :refer-macros [match]]
            [game.notification.requests :as requests]))

(defn fx-mark-all-server-read
  [gdb server-cid]
  (let [request-dispatch (requests/mark-all-read :server server-cid)]
    {:db gdb
     :dispatch-n (list (:dispatch request-dispatch)
                       [:game|server|notification|mark-all-read server-cid])}))

(he/reg-event-fx
 :game|notifications|mark-all-read
 (fn [{gdb :db} [_ identifier]]
   (match identifier
          [:server server-cid] (fx-mark-all-server-read gdb server-cid))))

(he/reg-event-fx
 :game|notification|req-read-all-ok
 (fn [{gdb :db} [_ fun result xargs]]
   (fun gdb result xargs)))
(he/reg-event-fx
 :game|notification|req-read-all-fail
 (fn [{gdb :db} [_ fun result xargs]]
   (fun gdb result xargs)))
