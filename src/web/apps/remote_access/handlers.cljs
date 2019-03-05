(ns web.apps.remote-access.handlers
  (:require [he.core :as he]
            [web.apps.db :as apps.db]
            [web.apps.remote-access.db :as remote-access.db]
            [web.apps.remote-access.validators :as v]))

(def validator (partial v/v v/state))

;; Helix Event Handlers

(defn- password-acquired-reducer
  [pass apps-db app-id]
  (apps.db/update-db apps-db app-id
                     #(remote-access.db/auth-update-pass % pass) nil))

(he/reg-event-fx
 :web|apps|remote-access|on-password-acquired
 (fn [{gdb :db} [_ {ip :server_ip pass :password}]]
   (let [apps-db (apps.db/get-context gdb)
         open-apps (apps.db/filter-by-type apps-db :remote-access)
         state-filter #(= (:ip %) ip)
         relevant-apps (apps.db/filter-by-state open-apps state-filter)
         relevant-ids (reduce (fn [acc [app-id _]]
                                (conj acc app-id)) [] relevant-apps)
         apps-db-reducer (partial password-acquired-reducer pass)
         new-apps-db (reduce apps-db-reducer apps-db relevant-ids)
         dispatch-event (if (empty? relevant-ids)
                          [:web|wm|app|open :remote-access {:screen :auth
                                                            :ip ip
                                                            :auth-pass pass}]
                          [:web|wm|window|focus (first relevant-ids)])]
     {:db (apps.db/set-context gdb new-apps-db)
      :dispatch-n (list dispatch-event)})))

;; Requests

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

