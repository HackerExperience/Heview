(ns game.server.process.db
  (:require [he.utils]
            [game.server.process.js :as process.js]
            [game.server.process.db.type :as process.db.type]))

;; Context

(defn get-context
  [global-db server-cid]
  (get-in global-db [:game :server server-cid :process]))

(defn set-context
  [global-db server-cid updated-local-db]
  (assoc-in global-db [:game :server server-cid :process] updated-local-db))

;; Model

(defn build-process-progress
  [progress]
  {:completion-date (:completion_date progress)
   :creation-date (:creation_date progress)
   :percentage (:percentage progress)
   :percentage-rate (:percentage_rate progress)})

(defn build-process-data
  [process]
  {:file {:source (:source_file process)
          :target (:target_file process)}})

(defn build-process
  [process]
  (cljs.pprint/pprint process)
  {:type (:type process)
   :progress (build-process-progress (:progress process))
   :target-ip (:target_ip process)
   :origin-ip (:origin_ip process)
   :data (build-process-data process)})

(defn format-process
  [raw-process]
  (let [process (build-process raw-process)
        client-meta (process.db.type/generate-client-meta process)]
    (assoc process :client-meta client-meta)))

;; Bootstrap

(defn bootstrap-server-reducer
  [acc process]
  (let [process-id (he.utils/get-canonical-id (:process_id process))]
    (into
     (sorted-map-by >)
     (assoc acc process-id (format-process process)))))

(defn bootstrap-server
  [db data]
  (let [process-entries (reduce bootstrap-server-reducer {} data)]
    (process.js/register-process-timers process-entries)
    (assoc-in db [:process :entries] process-entries)))

;; Recalcado

(defn on-top-recalcado
  [db processes]
  (let [process-entries (reduce bootstrap-server-reducer {} processes)]
    (process.js/deregister-process-timers process-entries)
    (process.js/register-process-timers process-entries)
    (assoc db :entries process-entries)))
