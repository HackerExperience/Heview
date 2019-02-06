(ns game.server.log.db
  (:require [he.date]))

;; Bootstrap

(defn format-log
  [log]
  (let [{timestamp :timestamp
         type :type
         data :data} log]
    {:datetime (he.date/timestamp-to-datetime timestamp)
     :type type
     :data data
     :text "Log text"}))

(defn bootstrap-log-reducer
  "Reduces the raw bootstrap response into the proper internal format.
  Removes the `log_id` information, since it's already present at the map key."
  [acc log]
  (into
   (sorted-map-by >)
   (assoc acc (:log_id log) (format-log log))))

(defn bootstrap-server
  [db data]
  (-> db
      (assoc-in [:log :entry] (reduce bootstrap-log-reducer {} data))))

;; Model

(defn get-sorted-entries
  [entries sorted]
  (map
   (fn [log_id]
     [log_id (get entries log_id)])
   sorted))

(defn add-new-log
  [db log]
  (let [new-logs (conj (:entry db) {(:log_id log) (format-log log)})
        new-sorted (conj (:sorted db) (:log_id log))]
    (-> db
        (assoc-in [:entry] new-logs)
        (assoc-in [:sorted] new-sorted))))
