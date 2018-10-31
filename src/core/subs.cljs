(ns core.subs
  (:require [re-frame.core :as rf]
            [home.subs]
            [game.subs]
            [setup.subs]))
(rf/reg-sub
 :game
 game.subs/game)

(rf/reg-sub
 :home
 home.subs/home)

(rf/reg-sub
 :setup
 setup.subs/setup)

(rf/reg-sub
  :core|state
  (fn [db _]
    (:state db)))
