(ns core.subs
  (:require [re-frame.core :as rf]
            [game.subs]))
(rf/reg-sub
  :game game.subs/game)

