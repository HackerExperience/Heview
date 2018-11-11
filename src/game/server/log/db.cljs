(ns game.server.log.db)

(defn log-reducer
  "Reduces the raw bootstrap response into the proper internal format.
  Removes the `log_id` information, since it's already present at the map key."
  [acc log]
  (assoc acc (:log_id log) (dissoc log :log_id)))

(defn bootstrap-server
  [db data]
  (-> db
      (assoc-in [:logs :entries] (reduce log-reducer {} data))))


