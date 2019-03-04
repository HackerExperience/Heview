(ns web.apps.remote-access.handlers
  (:require [he.core :as he]
            [web.apps.db :as apps.db]
            [web.apps.remote-access.db :as remote-access.db]
            [web.apps.remote-access.validators :as v]))

(def validator (partial v/v v/state))

;; Browse ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-db
 :web|apps|remote-access|browse|ip|change
 (fn [gdb [_ app-id new-ip]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db ldb app-id
                        #(remote-access.db/browse-ip-change % new-ip)
                        validator)
     (apps.db/set-context gdb ldb))))

(he/reg-event-fx
 :web|apps|remote-access|browse|submit
 (fn [{gdb :db} [_ app-id]]
   (let [ldb (apps.db/get-context gdb)
         new-gdb (as-> ldb ldb
                   (apps.db/update-db ldb app-id
                                      #(remote-access.db/browse-submit %)
                                      validator)
                   (apps.db/set-context gdb ldb))
         state (apps.db/get-state ldb app-id)
         server-cid (remote-access.db/get-server-cid state)
         ip (remote-access.db/get-ip state)
         callback (remote-access.db/browse-submit-callback app-id ip)]
     {:db new-gdb
      :dispatch [:game|network|browse-ip server-cid ip callback]})))

(he/reg-event-fx
 :web|apps|remote-access|browse|submit|ok
 (fn [{gdb :db} [_ _ _ _ [app-id _]]]
   {:db (as-> (apps.db/get-context gdb) ldb
          (apps.db/update-db ldb app-id
                             #(remote-access.db/browse-submit-ok %)
                             validator)
          (apps.db/set-context gdb ldb))
    :dispatch-n (list [:web|wm|window|resize app-id {:x 200 :y 160}]
                      [:web|wm|window|retitle app-id "Login"])}))

(he/reg-event-fx
 :web|apps|remote-access|browse|submit|fail
 (fn [{gdb :db} [_ status _response _gargs [app-id ip]]]
   (let [new-gdb (as-> (apps.db/get-context gdb) ldb
                   (apps.db/update-db ldb app-id
                                      #(remote-access.db/browse-submit-fail %)
                                      validator)
                   (apps.db/set-context gdb ldb))
         confirm-config (remote-access.db/browse-submit-fail-config
                         status app-id ip)]
     {:db new-gdb
      :dispatch [:web|wm|perform [:confirm app-id confirm-config]]})))

;; Auth ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-db
 :web|apps|remote-access|auth|pass|change
 (fn [gdb [_ app-id new-pass]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db ldb app-id
                        #(remote-access.db/auth-pass-change % new-pass)
                        validator)
     (apps.db/set-context gdb ldb))))

(he/reg-event-fx
 :web|apps|remote-access|auth|submit
 (fn [{gdb :db} [_ app-id]]
   (let [ldb (apps.db/get-context gdb)
         new-gdb (as-> ldb ldb
                   (apps.db/update-db ldb app-id
                                      #(remote-access.db/auth-submit %)
                                      validator)
                   (apps.db/set-context gdb ldb))
         state (apps.db/get-state ldb app-id)
         server-cid (remote-access.db/get-server-cid state)
         ip (remote-access.db/get-ip state)
         pass (remote-access.db/auth-get-pass state)
         callback (remote-access.db/auth-submit-callback app-id pass)]
     {:db new-gdb
      :dispatch [:game|server|login server-cid ip pass callback]})))


(he/reg-event-db
 :web|apps|remote-access|auth|submit|ok
 (fn [gdb [_ _ _ _ [app-id _]]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db ldb app-id
                         #(remote-access.db/auth-submit-ok %)
                         validator)
     (apps.db/set-context gdb ldb))))

(he/reg-event-fx
 :web|apps|remote-access|auth|submit|fail
 (fn [{gdb :db} [_ status _response _gargs [app-id pass]]]
   (let [new-gdb (as-> (apps.db/get-context gdb) ldb
                   (apps.db/update-db ldb app-id
                                      #(remote-access.db/auth-submit-fail %)
                                      validator)
                   (apps.db/set-context gdb ldb))
         confirm-config (remote-access.db/auth-submit-fail-config
                         status app-id pass)]
     {:db new-gdb
      :dispatch [:web|wm|perform [:confirm app-id confirm-config]]})))

