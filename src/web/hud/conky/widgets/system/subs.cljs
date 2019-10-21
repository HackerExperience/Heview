(ns web.hud.conky.widgets.system.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]
            [web.hud.conky.widgets.system.db :as system.db]))

(rf/reg-sub
 :web|hud|conky|widget|system|uptime-diff
 :<- [:web|os|boot-time]
 (fn [boot-time]
   (system.db/get-uptime-diff boot-time)))
