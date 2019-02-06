(ns game.db
  (:require [game.account.db]
            [game.story.db]
            [game.server.db]))

(defn set-csrf-token
  [db csrf-token]
  (assoc-in db [:game :meta :csrf-token] csrf-token))

(defn get-csrf-token
  [db]
  (get-in db [:game :meta :csrf-token]))

(defn bootstrap-account
  [db bootstrap]
  (-> db
      (game.account.db/bootstrap-account (:account bootstrap))
      (game.story.db/bootstrap-account (:storyline bootstrap))
      (game.server.db/bootstrap-account (:servers bootstrap))))

(defn bootstrap-server
  [db [server-cid bootstrap]]
  (-> db
      (game.server.db/bootstrap-server bootstrap server-cid)))
