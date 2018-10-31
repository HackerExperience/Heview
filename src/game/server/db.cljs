(ns game.server.db)

;; (defn register-meta-player
;;   [db data]
;;   (assoc-in db [:lolwut] 
;;     (for [meta-player data]
;;         (conj [meta-player]))))

;; (defn register-meta-remote
;;   [db data]
;;   db)

(defn initial
  [db data]
  (-> db
      (assoc-in [:game :server :meta] data)))
