(ns web.apps.db)

;; Model ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn fetch
  [db app-id]
  (get-in db [:web :apps app-id]))

(defn get-context
  ([app]
   (get-in app [:meta :context]))
  ([db app-id]
   (get-context (fetch db app-id))))

(defn get-other-context
  ([app]
   (if (= :local (get-context app))
     :remote
     :local))
  ([db app-id]
   (get-other-context (fetch db app-id))))

(defn get-type
  ([app]
   (get-in app [:meta :type]))
  ([db app-id]
   (get-type (fetch db app-id))))

(defn get-state
  ([app]
   (get-in app [:state :current]))
  ([db app-id]
   (get-state (fetch db app-id))))

(defn get-other-state
  ([app]
   (get-in app [:state :other]))
  ([db app-id]
   (get-state (fetch db app-id))))

(defn add-meta
  [db session-id app-id app-type context]
  (assoc-in
   db
   [:web :apps app-id :meta]
   {:session session-id
    :context context
    :active-context context ;; TODO: This context should be the session-id?
    :type app-type}))

(defn update-state
  [db new-state app-id]
  (assoc-in db [:web :apps app-id :state :current] new-state))

(defn update-other-state
  [db new-state app-id]
  (assoc-in db [:web :apps app-id :state :other] new-state))

(defn update-db
  [old-db app-id callback]
  (as-> old-db new-db
      (get-state old-db app-id)
      (callback new-db)
      (update-state old-db new-db app-id)))

(defn update-meta-context
  [db app-id app]
  (let [other-context (get-other-context app)]
    (-> db
        ;; TODO: Also need to update the `active-context` entry with the new cid
        (assoc-in [:web :apps app-id :meta :context] other-context))))

(defn switch-context
  [db app-id]
  (let [app (fetch db app-id)
        current-state (get-state app)
        other-state (get-other-state app)] ;; TODO: Initialize `other` properly
    (as-> db db
      (update-state db other-state app-id)
      (update-other-state db current-state app-id)
      (update-meta-context db app-id app))))
