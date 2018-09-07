(ns game.server.subs
  (:require [re-frame.core :as rf]))

(defn server
  [db _]
  (:server db))

(rf/reg-sub
 :game|server|get-ip
 :<- [:game|server]
 (fn [db _]
   (:ip db)))

