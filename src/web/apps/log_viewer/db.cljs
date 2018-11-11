(ns web.apps.log-viewer.db)

(defn initial
  []
  {:foo :bar})

(defn on-open
  []
  [:ok (initial)])

(defn on-close
  [app-state]
  [:ok])
