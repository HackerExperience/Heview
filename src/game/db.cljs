(ns game.db
  (:require [game.account.db :as account.db]
            [game.story.db :as story.db]
            [game.server.db :as server.db]))

;; Context

(defn get-context
  [global-db]
  (get-in global-db [:game]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:game] updated-local-db))

;; Model

(defn set-csrf-token
  [db csrf-token]
  (assoc-in db [:meta :csrf-token] csrf-token))

(defn get-csrf-token
  [db]
  (get-in db [:meta :csrf-token]))

;; (defn bootstrap-account
;;   [db bootstrap]
;;   (-> db
;;       (game.account.db/bootstrap-account (:account bootstrap))
;;       (game.story.db/bootstrap-account (:storyline bootstrap))
;;       (game.server.db/bootstrap-account (:servers bootstrap))))

(defn bootstrap-account
  [db bootstrap]
  (let [bs-acc-server (server.db/bootstrap-account (:servers bootstrap))
        bs-acc-account (account.db/bootstrap-account (:account bootstrap))
        bs-acc-story (story.db/bootstrap-account (:storyline bootstrap))]
    (-> db
        (server.db/set-context bs-acc-server)
        (account.db/set-context bs-acc-account)
        (story.db/set-context bs-acc-story))))

(defn bootstrap-server-first-pass
  [server-db [server-cid bootstrap]]
  (server.db/bootstrap-server server-db bootstrap server-cid))

(defn bootstrap-server-second-pass
  [server-db bootstrap]
  (let [server-index (get-in bootstrap [:account :servers])
        player-index (:player server-index)
        remote-index (:remote server-index)]
    (as-> server-db db
        (reduce server.db/bootstrap-server-add-endpoints db player-index)
        (reduce server.db/bootstrap-server-add-link db remote-index))))

(defn bootstrap-server
  [gdb full-bootstrap]
  (as-> (server.db/get-context gdb) server-db
    (reduce bootstrap-server-first-pass server-db (:servers full-bootstrap))
    ;(bootstrap-server-second-pass server-db full-bootstrap)
    (server.db/set-context gdb server-db)))
