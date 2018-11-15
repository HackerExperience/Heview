(ns boot.db)

(def initial
  {:loading? true
   :failed? false})

(defn boot-flow-failed
  [db]
  (-> db
      (assoc-in [:boot :loading?] false)
      (assoc-in [:boot :failed?] true)))
