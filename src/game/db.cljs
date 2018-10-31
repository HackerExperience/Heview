(ns game.db
  (:require [game.story.db]
            [game.server.db]))

(def initial
  {:server {:ip 1}})

(defn bootstrap-account
  [db data]
  (-> db
      (game.story.db/initial (:storyline data))
      (game.server.db/initial (:servers data))))

;; (defn bootstrap
;;   [data]
;;   {:server (game.server.db/boostrap data.server)
;;    :account (game.account.db/boostrap data.account)
;;    :client (game.client.db/boostrap data)})
