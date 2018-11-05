(ns game.db
  (:require [game.story.db]
            [game.server.db]))

(defn bootstrap-account
  [db data]
  (-> db
      (game.story.db/bootstrap-account (:storyline data))
      (game.server.db/bootstrap-account (:servers data))))

(defn bootstrap-server
  [db data server-cid]
  (-> db
      (game.server.db/bootstrap-server data server-cid)))

;; (defn bootstrap
;;   [data]
;;   {:server (game.server.db/boostrap data.server)
;;    :account (game.account.db/boostrap data.account)
;;    :client (game.client.db/boostrap data)})
