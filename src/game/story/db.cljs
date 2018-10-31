(ns game.story.db)

(defn initial
  [db data]
  (assoc-in db [:game :story] data))
