(ns game.story.db)

;; Context

(defn get-context
  [global-db]
  (get-in global-db [:game :story]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:game :story] updated-local-db))

;; Bootstrap

(defn bootstrap-account
  [data]
  data)
