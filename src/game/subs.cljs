(ns game.subs
  (:require [re-frame.core :as rf]
            [game.server.subs]))

(defn game
  [db _]
  (:game db))

(rf/reg-sub
  :game|server
  :<- [:game]
  game.server.subs/server)
