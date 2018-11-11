(ns web.wm.db
  (:require [cljs.core.match :refer-macros [match]]
            [he.utils]
            [web.wm.windowable]))

(def initial
  {})

;; Model ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-active-session
  [db]
  (get-in db [:web :wm :active-session]))

(defn get-app
  [db app-id]
  (get-in db [:web :wm :apps app-id]))

(defn get-app-context
  ([app]
   (get-in app [:meta :context]))
  ([db app-id]
   (get-app-context (get-app db app-id))))

(defn get-app-other-context
  ([app]
   (if (= :local (get-app-context app))
     :remote
     :local))
  ([db app-id]
   (get-app-other-context (get-app db app-id))))

(defn get-app-type
  ([app]
   (get-in app [:meta :type]))
  ([db app-id]
   (get-app-type (get-app db app-id))))

(defn get-app-state
  ([app]
   (get-in app [:state :current]))
  ([db app-id]
   (get-app-state (get-app db app-id))))

(defn get-app-other-state
  ([app]
   (get-in app [:state :other]))
  ([db app-id]
   (get-app-state (get-app db app-id))))

(defn update-app-state
  [new-state db app-id]
  (assoc-in db [:web :wm :apps app-id :state :current] new-state))

(defn update-app-other-state
  [new-state db app-id]
  (assoc-in db [:web :wm :apps app-id :state :other] new-state))

(defn update-app-db
  [db app-id callback]
  (-> db
      (get-app-state app-id)
      (callback)
      (update-app-state db app-id)))

(defn update-meta-context
  [db app-id app]
  (let [other-context (get-app-other-context app)]
    (-> db
        ;; TODO: Also need to update the `active-context` entry with the new cid
        (assoc-in [:web :wm :apps app-id :meta :context] other-context))))

(defn switch-context
  [db app-id]
  (let [app (get-app db app-id)
        current-state (get-app-state app)
        other-state (get-app-other-state app)] ;; TODO: Initialize `other` properly
    (as-> db db
      (update-app-state other-state db app-id)
      (update-app-other-state current-state db app-id)
      (update-meta-context db app-id app))))

;; Bootstrap ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn initial-session-state
  [gateway]
  {:gateway (:server_id gateway)
   :endpoint nil
   :apps []})

(defn reducer-init-local-sessions
  [acc-db gateway]
  (assoc-in
   acc-db
   [:web :wm :sessions (:server_id gateway)]
   (initial-session-state gateway)))

(defn init-local-sessions
  [db gateways]
  (reduce reducer-init-local-sessions db gateways))

(defn bootstrap
  [db gateways mainframe]
  (-> db
      (init-local-sessions gateways)
      (assoc-in [:web :wm :active-session] (:server_id mainframe))))

;; App ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; App > Open ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn add-app-state
  [db state app-id]
  (assoc-in db [:web :wm :apps app-id :state :current] state))

(defn add-app-meta
  [db session-id app-id app-type context]
  (assoc-in
    db
    [:web :wm :apps app-id :meta]
    {:session session-id
     :context context
     :active-context context ;; TODO: This context should be the session-id?
     :type app-type}))

(defn initial-window-data
  []
  {:x 50
   :y 50})

(defn add-window-data
  [db app-id]
  (assoc-in db [:web :wm :apps app-id :window] (initial-window-data)))

(defn add-app-entry
  [db session-id app-id]
  (update-in db [:web :wm :sessions session-id :apps] #(vec (conj % app-id))))

(defn on-open-ok
  [db app-type data]
  (let [session-id (get-active-session db)
        app-id (str (random-uuid))
        context :local]
    (-> db
        (add-app-state data app-id)
        (add-app-meta session-id app-id app-type context)
        (add-window-data app-id)
        (add-app-entry session-id app-id)
        (->>
         (vector :ok)))))

(defn on-open-error
  [_db reason]
  [:error reason])

(defn open
  " Colocar:
  - App data into `[:web :apps :state $id]`
  - App mapping into `[:web :apps :meta $id]`
  - Window data into `[:web :wm $id :window]`
  - App entry into `[:web :wm :sessions $session-id :apps]`
  "
  [db app-type]
  (match (web.wm.windowable/open app-type)
         [:ok data] (on-open-ok db app-type data)
         [:error reason] (on-open-error db reason)))

;; App > Close ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn remove-app-entry
  [db session-id app-id]
  (assoc-in
   db
   [:web :wm :sessions session-id :apps]
   (remove #(= app-id %) (get-in db [:web :wm :sessions session-id :apps]))))

(defn on-close-ok
  [db app-id]
  (let [session-id (get-active-session db)]
    (-> db
        (he.utils/dissoc-in [:web :wm :apps app-id])
        (remove-app-entry session-id app-id)
        (->>
         (vector :ok)))))

(defn on-close-error
  [db reason]
  [:error reason])

(defn close
  "Remove whatever was inserted on `open`"
  [db app-id]
  (let [app (get-app db app-id)
        app-type (get-app-type app)
        app-state (get-app-state app)]
    (match (web.wm.windowable/close app-type app-state)
          [:ok] (on-close-ok db app-id)
          [:error reason] (on-close-error db reason))))
