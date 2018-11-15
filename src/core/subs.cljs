(ns core.subs
  (:require [re-frame.core :as rf]
            [boot.subs]
            [game.subs]
            [web.subs]))

(rf/reg-sub
 :game
 game.subs/game)

(rf/reg-sub
 :boot
 boot.subs/boot)

(rf/reg-sub
 :web
 web.subs/web)

(rf/reg-sub
 :core
 (fn [db _]
   (:core db)))

(rf/reg-sub
 :core|state
 :<- [:core]
 (fn [db _]
   (:state db)))
