(ns web.hud.conky.widgets.processes.db)

;; (defn conj-sort
;;   [acc []])

(defn format-timed-entries-reducer
  [acc [process-id process]]
  (let [client-meta (:client-meta process)
        completion-date (:completion-date (:progress process))
        entry {:process-id process-id
               :name (:action-str client-meta)
               :note (:tm-note client-meta)
               :progress (:progress process)}]
    (into
     (sorted-map-by <)
     (assoc acc completion-date entry))))

(defn format-timed-entries
  [entries]
  (or (vals (reduce format-timed-entries-reducer {} entries))
      []))
