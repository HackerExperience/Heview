(ns web.apps.chat.handlers
  (:require [he.core :as he]
            [web.apps.db :as apps.db]
            [web.apps.chat.db :as chat.db]))

(he/reg-event-fx
 :web|apps|chat|story|reply
 (fn [{gdb :db} [_ app-id reply-id]]
   (let [ldb (apps.db/get-context gdb)
         new-gdb (as-> ldb ldb
                   (apps.db/update-db ldb app-id
                                      #(chat.db/story-reply-submit %)
                                      nil)
                   (apps.db/set-context gdb ldb))
         state (apps.db/get-state ldb app-id)
         contact-id (chat.db/get-contact-id state)
         callback (chat.db/story-reply-callback app-id)]
     {:db new-gdb
      :dispatch [:game|story|reply contact-id reply-id callback]})))

(he/reg-event-db
 :web|apps|chat|story|reply|ok
 (fn [gdb [_ _ _ _ [app-id]]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db ldb app-id
                        #(chat.db/story-reply-response-ok %)
                        nil)
     (apps.db/set-context gdb ldb))))

(he/reg-event-fx
 :web|apps|chat|story|reply|fail
 (fn [{gdb :db} [_ status _ _ [app-id]]]
   (let [new-gdb (as-> (apps.db/get-context gdb) ldb
                   (apps.db/update-db
                    ldb app-id
                    #(chat.db/story-reply-response-fail % status)
                    nil)
                   (apps.db/set-context gdb ldb))
         confirm-config (chat.db/story-reply-fail-config status app-id)]
     {:db new-gdb
      :dispatch [:web|wm|perform [:confirm app-id confirm-config]]})))
