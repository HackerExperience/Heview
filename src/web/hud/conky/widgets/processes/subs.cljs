(ns web.hud.conky.widgets.processes.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]
            [web.hud.conky.widgets.processes.db :as processes.db]))

(rf/reg-sub
 :web|hud|conky|widget|processes|entries|timed
 (fn [[_ server-cid]]
   [(he/subscribed [:game|server|process|entries|timed server-cid])])
 (fn [[entries]]
   (processes.db/format-timed-entries entries)))
