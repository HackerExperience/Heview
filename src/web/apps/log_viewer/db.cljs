(ns web.apps.log-viewer.db)

(defn initial
  []
  {:counter 0})

(defn counter-inc
  [state]
  (update state :counter #(inc %)))

(defn counter-dec
  [state]
  (update state :counter #(dec %)))

;; Interface

(defn ^:export on-open
  []
  [:ok (initial)])

(defn ^:export on-close
  [app-state]
  [:ok])
