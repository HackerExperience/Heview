(ns web.hemacs.handlers
  (:require [he.core :as he]
            [web.apps.db :as apps.db]
            [web.hemacs.db :as hemacs.db]))

(he/reg-event-db
 :web|hemacs|process-input
 (fn [gdb [_ key]]
   (let [nice-key (if (= key " ")
                    "Space"
                    key)]
     (as-> (hemacs.db/get-context gdb) ldb
       (hemacs.db/process-input gdb ldb nice-key)
       (hemacs.db/set-context gdb ldb)))))

(defn- get-focused-app-type
  [app-db app-id]
  (when-not (nil? app-id)
    (let [app-type (apps.db/get-type app-db app-id)
          app-popup (apps.db/get-popup app-db app-id)]
      (if (= app-type :popup)
        [(:app-type app-popup) (:popup-type app-popup)]
        [app-type]))))

(he/reg-event-db
 :web|hemacs|find-mode
 (fn [gdb [_ wm-state]]
   (let [app-db (apps.db/get-context gdb)
         session-id (:session-id wm-state)
         focused-app-id (:focused-app wm-state)
         focused-app-type (get-focused-app-type app-db focused-app-id)
         state-params {:focused-app-type focused-app-type
                       :focused-app-id focused-app-id
                       :session-id session-id}]
     (as-> (hemacs.db/get-context gdb) ldb
       (hemacs.db/find-mode ldb state-params)
       (hemacs.db/set-context gdb ldb)))))

(he/reg-event-db
 :web|hemacs|input-focused-in
 (fn [gdb _]
   (println "Focusing in")
   (as-> (hemacs.db/get-context gdb) ldb
     (hemacs.db/input-focused-in ldb)
     (hemacs.db/set-context gdb ldb))))

(he/reg-event-db
 :web|hemacs|input-focused-out
 (fn [gdb _]
   (println "Focusing out")
   (as-> (hemacs.db/get-context gdb) ldb
     (hemacs.db/input-focused-out ldb)
     (hemacs.db/set-context gdb ldb))))

(he/reg-event-db
 :web|hemacs|bootstrap
 (fn [gdb _]
   (hemacs.db/set-context gdb (hemacs.db/bootstrap))))
