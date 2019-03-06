(ns web.apps.task-manager.db)

(def open-opts
  {:len-x 580
   :len-y 400
   :config {:icon-class "fas fa-tasks"
            :title "Task Manager"}})

(defn initial []
  {:selected nil})

;; Model

(defn group-by-ip-reducer
  [acc [process-id process]]
  (let [target-ip (:target-ip process)
        target-map (get acc target-ip {})
        new-target-map (merge
                        target-map
                        (hash-map process-id process))]
    (merge
     acc
     (hash-map target-ip new-target-map))))

(defn select-entry
  [db process-id]
  (assoc db :selected process-id))

(defn deselect-entry
  [db]
  (assoc db :selected nil))

(defn get-selected-id
  [db]
  (:selected db))

;; Interface

;; Events API

(defn ^:export on-click
  [db process-id]
  (let [selected-id (get-selected-id db)]
    (if (= selected-id process-id)
      (deselect-entry db)
      (select-entry db process-id))))

;; WM API

(defn ^:export did-open
  [_ctx app-context]
  [:ok (initial) open-opts])
