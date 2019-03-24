(ns game.story.requests
  (:require [driver.rest.request :as request]
            [game.story.db :as story.db]))

(defn on-reply-ok
  [gdb [status response] [{callback :on-ok} contact-id reply-id]]
  (println "okkkk " contact-id reply-id)
  (let [new-gdb (as-> (story.db/get-context gdb) ldb
                  (story.db/on-reply-sent ldb contact-id reply-id)
                  (story.db/set-context gdb ldb))]
    {:db new-gdb
     :dispatch (request/callback-event callback status response)}))

(defn on-reply-fail
  [_db [status response] [{callback :on-fail}]]
  (request/wrap-callback callback status response))

(defn reply
  [contact-id reply-id callback]
  (request/story-reply
   contact-id reply-id
   {:on-ok [:game|story|reply|ok on-reply-ok callback contact-id reply-id]
    :on-fail [:game|story|reply|fail on-reply-fail callback]}))
