(ns web.hud.conky.handlers
  (:require [he.core :as he]
            [web.hud.conky.db :as conky.db]))

(he/reg-event-db
 :web|hud|conky|bootstrap
 (fn [gdb _]
   (conky.db/set-context gdb (conky.db/bootstrap))))
