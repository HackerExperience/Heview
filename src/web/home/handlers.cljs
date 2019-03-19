(ns web.home.handlers
  (:require [he.core :as he]
            [core.db]
            [web.home.login.handlers]
            [web.install.db :as install.db]))


(he/reg-event-db
 :web|home|signup|signup
 (fn [gdb _]
   (core.db/switch-mode gdb :home :install install.db/initial)))

