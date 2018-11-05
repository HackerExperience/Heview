(ns game.server.subs
  (:require [re-frame.core :as rf]))

(defn server
  [db _]
  (:server db))

(rf/reg-sub
 :game|server|meta
 :<- [:game|server]
 (fn [db _]
   (:meta db)))

;; TODO: Player online (skipping `remote`)
(rf/reg-sub
 :game|server|joinable
 :<- [:game|server|meta]
 (fn [meta _]
   (into []
      (for [meta-player (:player meta)]
        (conj (:server_id meta-player))))))

