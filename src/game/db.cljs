(ns game.db
  (:require [game.story.db]
            [game.server.db]))

(defn bootstrap-account
  [db bootstrap]
  (-> db
      (game.story.db/bootstrap-account (:storyline bootstrap))
      (game.server.db/bootstrap-account (:servers bootstrap))))

(defn bootstrap-server
  [db [server-cid bootstrap]]
  (-> db
      (game.server.db/bootstrap-server bootstrap server-cid)))
