(ns game.subs
  (:require [re-frame.core :as rf]
            [game.server.subs]
            [game.story.subs]))

(defn game
  [db _]
  (:game db))

(rf/reg-sub
  :game|server
  :<- [:game]
  game.server.subs/server)

(rf/reg-sub
  :game|story
  :<- [:game]
  game.story.subs/story)

(rf/reg-sub
 :game|tick
 (fn [db _]
   (:tick db)))
