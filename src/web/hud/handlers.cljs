(ns web.hud.handlers
  (:require [he.core :as he]
            [web.hud.db :as hud.db]))

(he/reg-event-db
 :web|hud|bootstrap
 (fn [gdb _]
   (hud.db/set-context gdb (hud.db/bootstrap))))

(he/reg-event-db
 :web|hud|launcher|open-overlay
 (fn [gdb _]
   (as-> (hud.db/get-context gdb) ldb
     (hud.db/launcher-open-overlay ldb)
     (hud.db/set-context gdb ldb))))

(he/reg-event-db
 :web|hud|launcher|close-overlay
 (fn [gdb _]
   (as-> (hud.db/get-context gdb) ldb
     (hud.db/launcher-close-overlay ldb)
     (hud.db/set-context gdb ldb))))
