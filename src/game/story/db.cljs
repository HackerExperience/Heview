(ns game.story.db)

(defn bootstrap-account
  [db data]
  (assoc-in db [:game :story] data))
