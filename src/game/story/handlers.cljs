(ns game.story.handlers
  (:require [he.core :as he]
            [game.story.db :as story.db]
            [game.story.requests :as story.requests]))

;; Events

(he/reg-event-db
 :game|story|email-sent
 (fn [gdb [_ email]]
   (as-> (story.db/get-context gdb) ldb
     (story.db/on-email-sent ldb email)
     (story.db/set-context gdb ldb))))

;; This reacts to the `OnReplySent` event, which confirms that the player's
;; response was registered. We do not have to add the message here, because we
;; already added it "optimistically" on the original reply request
;; (`:game|story|reply`). What we do have to do is extract the new progress and
;; save it on the local db.
(he/reg-event-db
 :game|story|reply-sent
 (fn [gdb [_ reply]]
   (as-> (story.db/get-context gdb) ldb
     (story.db/on-reply-confirmation ldb reply)
     (story.db/set-context gdb ldb))))

;; Requests

(he/reg-event-fx
 :game|story|reply
 (fn [{gdb :db} [_ contact-id reply-id callback]]
   (story.requests/reply contact-id reply-id callback)))

(he/reg-event-fx
 :game|story|reply|ok
 (fn [{gdb :db} [_ fun result xargs]]
   (fun gdb result xargs)))
(he/reg-event-fx
 :game|story|reply|fail
 (fn [{gdb :db} [_ fun result xargs]]
   (fun gdb result xargs)))
