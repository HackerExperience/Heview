(ns web.apps.db
  (:require [cljs.core.match :refer-macros [match]]
            [he.utils]))

;; Context ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-context
  [global-db]
  (get-in global-db [:web :apps]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:web :apps] updated-local-db))

;; Model ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn fetch
  [db app-id]
  (get-in db [app-id]))

(defn get-app-context
  ([app]
   (get-in app [:meta :context]))
  ([db app-id]
   (get-app-context (fetch db app-id))))

(defn get-other-context
  ([app]
   (if (= :local (get-app-context app))
     :remote
     :local))
  ([db app-id]
   (get-other-context (fetch db app-id))))

(defn get-meta
  ([app]
   (get-in app [:meta]))
  ([db app-id]
   (get-meta (fetch db app-id))))

(defn get-type
  ([app]
   (get-in app [:meta :type]))
  ([db app-id]
   (get-type (fetch db app-id))))

(defn get-popup
  ([app]
   (get-in app [:meta :popup]))
  ([db app-id]
   (get-popup (fetch db app-id))))

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

(defn derive-popup-entry
  [app-type popup-info]
  (if-not (nil? popup-info)
    (let [[popup-type parent-app-id] popup-info]
      {:parent-id parent-app-id
       :app-type app-type
       :popup-type popup-type})
    nil))

(defn add-meta
  [db session-id app-id app-type context popup-info]
  (let [popup-entry (derive-popup-entry app-type popup-info)
        public-app-type (if-not (nil? popup-entry)
                          :popup
                          app-type)]
    (assoc-in db [app-id :meta]
     {:session session-id
      :context context
      :active-context context ;; TODO: This context should be the session-id?
      :type public-app-type
      :popup popup-entry
      :children []})))

(defn add-child-entry
  [db parent-id child-id]
  ;; The `parent-id` may be nil in the case of some OS-based popups
  (if-not (nil? parent-id)
    (update-in db [parent-id :meta :children] #(conj % child-id))
    db))

(defn add-child-info
  [db child-id popup-info]
  (if-not (nil? popup-info)
    (let [[_ parent-id] popup-info]
      (add-child-entry db parent-id child-id))
    db))

(defn remove-child-entry
  [db parent-id child-id]
  (let [parent-app (fetch db parent-id)]
    (if-not (nil? parent-app)
      (update-in db
                 [parent-id :meta :children]
                 #(he.utils/vec-remove % (.indexOf % child-id)))
      db)))

(defn remove-child
  [db app-id]
  (let [{popup :popup} (get-meta db app-id)]
    (if-not (nil? popup)
      (remove-child-entry db (:parent-id popup) app-id)
      db)))

(defn update-state
  [db new-state app-id]
  (assoc-in db [app-id :state :current] new-state))

(defn update-other-state
  [db new-state app-id]
  (assoc-in db [app-id :state :other] new-state))

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
        (assoc-in [app-id :meta :context] other-context))))

(defn switch-context
  [db app-id]
  (let [app (fetch db app-id)
        current-state (get-state app)
        other-state (get-other-state app)] ;; TODO: Initialize `other` properly
    (as-> db db
      (update-state db other-state app-id)
      (update-other-state db current-state app-id)
      (update-meta-context db app-id app))))

;; Query

(defn query-reducer
  [db acc child-id]
  (conj acc (hash-map child-id (fetch db child-id))))

(defn query
  [db app-id]
  (let [app (fetch db app-id)
        children (get-in app [:meta :children])]
    (if-not (nil? app)
      (assoc app :children (reduce (partial query-reducer db) {} children))
      nil)))

(defn query-has-blocking-popups-reducer
  [_acc [child-id child-app]]
  (let [{app-type :app-type
         popup-type :popup-type} (get-popup child-app)]
    (match [app-type popup-type]
           [:os _] (reduced child-id)
           [_ _] false)))

(defn query-has-blocking-popups
  [query]
  (let [children (:children query)]
    (if-not (empty? (:children query))
      (reduce query-has-blocking-popups-reducer false children)
      false)))

;; Interface

(defn on-open
  [db app-id session-id app-type context state popup-info]
  (-> db
      (update-state state app-id)
      (add-meta session-id app-id app-type context popup-info)
      (add-child-info app-id popup-info)))

(defn on-close
  [db app-id]
  (-> db
      (remove-child app-id)
      (he.utils/dissoc-in [app-id])))
