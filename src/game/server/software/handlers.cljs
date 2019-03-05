(ns game.server.software.handlers
  (:require [he.core :as he]
            [game.server.software.db :as software.db]
            [game.server.software.requests :as software.requests]))


;; Request > Download

(he/reg-event-fx
 :game|server|software|download
 (fn [{gdb :db} [_ target-nip file-id callback]]
   (software.requests/file-download target-nip file-id callback)))

(he/reg-event-fx
 :game|server|software|req-download-ok
 (fn [{gdb :db} [_ fun result xargs]]
   (fun gdb result xargs)))
(he/reg-event-fx
 :game|server|software|req-download-fail
 (fn [{gdb :db} [_ fun result xargs]]
   (fun gdb result xargs)))

;; Request > Upload

(he/reg-event-fx
 :game|server|software|upload
 (fn [{gdb :db} [_ target-nip file-id callback]]
   (software.requests/file-upload target-nip file-id callback)))

(he/reg-event-fx
 :game|server|software|req-upload-ok
 (fn [{gdb :db} [_ fun result xargs]]
   (fun gdb result xargs)))
(he/reg-event-fx
 :game|server|software|req-upload-fail
 (fn [{gdb :db} [_ fun result xargs]]
   (fun gdb result xargs)))

;; Request > Bruteforce

(he/reg-event-fx
 :game|server|software|bruteforce
 (fn [{gdb :db} [_ gateway-id target-ip callback]]
   (software.requests/bruteforce gateway-id target-ip callback)))

(he/reg-event-fx
 :game|server|software|req-bruteforce-ok
 (fn [{gdb :db} [_ fun result xargs]]
   (fun gdb result xargs)))
(he/reg-event-fx
 :game|server|software|req-bruteforce-fail
 (fn [{gdb :db} [_ fun result xargs]]
   (fun gdb result xargs)))

