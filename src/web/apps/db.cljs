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

(def launcher-config
  [{:icon-class "fa fa-archive"
    :name "Log Viewer"
    :can-display-fn nil
    :on-click-event [:web|wm|app|open :log-viewer]}
   {:icon-class "fas fa-network-wired"
    :name "Remote Access"
    :can-display-fn nil
    :on-click-event [:web|wm|app|open :remote-access]}
   {:icon-class "fas fa-tasks"
    :name "Task Manager"
    :can-display-fn nil
    :on-click-event [:web|wm|app|open :task-manager]}
   {:icon-class "far fa-folder"
    :name "File Explorer"
    :can-display-fn nil
    :on-click-event [:web|wm|app|open :file-explorer]}
   {:icon-class "fab fa-firefox"
    :name "Web Browser"
    :can-display-fn nil
    :on-click-event [:web|wm|app|open :browser]}

   {:icon-class "far fa-folder"
    :name "Cracker (tmp)i"
    :can-display-fn nil
    :on-click-event [:web|wm|app|open :software-cracker]}
   ])

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
   (get-other-state (fetch db app-id))))

(defn derive-popup-entry
  [app-type popup-info]
  (if-not (nil? popup-info)
    (let [[popup-type parent-app-id] popup-info]
      {:parent-id parent-app-id
       :app-type app-type
       :popup-type popup-type})
    nil))

(defn add-meta
  [db session-id server-cid app-id app-type context popup-info]
  (let [popup-entry (derive-popup-entry app-type popup-info)
        public-app-type (if-not (nil? popup-entry)
                          :popup
                          app-type)]
    (assoc-in db [app-id :meta]
     {:session session-id
      :context context
      :context-cid server-cid
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

(defn validate-state
  [state validator]
  (if-not (nil? validator)
    (validator state)
    state))

(defn update-db
  [old-db app-id callback validator]
  (as-> old-db new-db
      (get-state old-db app-id)
      (callback new-db)
      (validate-state new-db validator)
      (update-state old-db new-db app-id)))

(defn with-app-state
  [db app-id callback]
  (-> db
      (get-state app-id)
      (callback)))

(defn update-meta-context
  [db app-id app new-context-cid]
  (let [other-context (get-other-context app)]
    (-> db
        (assoc-in [app-id :meta :context] other-context)
        (assoc-in [app-id :meta :context-cid] new-context-cid))))

(defn switch-context
  [db app-id new-context-cid]
  (let [app (fetch db app-id)
        current-state (get-state app)
        other-state (get-other-state app)]
    (as-> db db
      (update-state db other-state app-id)
      (update-other-state db current-state app-id)
      (update-meta-context db app-id app new-context-cid))))

;; Filters

(defn- filter-by-type-reducer
  [app-type acc [current-app-id current-app]]
  (let [current-app-type (get-in current-app [:meta :type])]
    (if (= app-type current-app-type)
      (conj acc [current-app-id current-app])
      acc)))

(defn filter-by-type
  [db app-type]
  (let [reducer (partial filter-by-type-reducer app-type)]
    (reduce reducer [] db)))

(defn- filter-by-state-reducer
  [filter-fn acc [app-id app]]
  (if (filter-fn (get-in app [:state :current]))
    (conj acc [app-id app])
    acc))

(defn filter-by-state
  [db filter-fn]
  (let [reducer (partial filter-by-state-reducer filter-fn)]
    (reduce reducer [] db)))

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
  [db app-id session-id server-cid app-type context state popup-info]
  (-> db
      (update-state state app-id)
      (add-meta session-id server-cid app-id app-type context popup-info)
      (add-child-info app-id popup-info)))

(defn on-close
  [db app-id]
  (-> db
      (remove-child app-id)
      (he.utils/dissoc-in [app-id])))

;; Windowable API (Default) ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn ^:export default-will-open
  [app-type [_ctx app-context args]]
  [:open-app app-type app-context args])
(defn ^:export default-did-open
  [_ [_ctx _app-context _args]]
  [:ok {} {}])

(defn ^:export default-will-close
  [_app-type [_ctx app-id _state _args]]
  [:close-app app-id])
(defn ^:export default-did-close
  [_app-type [_ctx _app-id _state _args]]
  [:ok])

(defn ^:export default-will-focus
  [_app-type [_ctx app-id _state _args]]
  [:focus app-id])
(defn ^:export default-did-focus
  [_]
  [:ok])

;; Windowable API (Default) > Popup handlers ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn ^:export popup-may-open
  [app-type [_ popup-type parent-id args]]
  [:open-popup app-type popup-type parent-id args])
(defn ^:export default-popup-may-close
  [app-type [_ctx popup-type family-ids _state args]]
  [:close-popup app-type popup-type family-ids args])
(defn ^:export default-popup-may-focus
  [app-type [_ctx popup-type family-ids _state args]]
  [:focus-popup app-type popup-type family-ids args])
