(ns web.apps.log-viewer.popups.log-edit.db
  (:require [he.utils]
            [game.server.log.db :as log.db]
            [web.apps.db :as apps.db]
            [web.wm.db :as wm.db]))

(def default-length
  {:len-x 400
   :len-y 400})

(defn open-opts
  [next-position]
  {:len-x (:len-x default-length)
   :len-y (:len-y default-length)
   :x (:x next-position)
   :y (:y next-position)})

(defn initial-state
  [game-db server-cid log-id]
  (let [log-db (log.db/get-context-game game-db server-cid)
        log (log.db/get-log log-db log-id)]
    {:server-cid server-cid
     :log-id log-id
     :log log
     :invalid-fields []
     :changed? false}))

(defn has-changed?
  [state]
  (:changed? state))

(defn update-valid-status
  [state field-id valid?]
  (if valid?
    (if (some #(= field-id %) (:invalid-fields state))
      (update-in state [:invalid-fields] #(he.utils/vec-remove % (.indexOf % field-id)))
      state)
    (if (some #(= field-id %) (:invalid-fields state))
      state
      (assoc-in state [:invalid-fields] (conj (:invalid-fields state) field-id)))))

;; Public API

;; Events API

(defn ^:export on-field-change
  [state field-id field-type new-field-value]
  (let [old-log (:log state)
        new-log (assoc-in old-log [:data field-id] new-field-value)
        new-log-value (log.db/derive-log-text new-log)
        new-log (assoc-in new-log [:value] new-log-value)
        valid? (log.db/validate-log-value field-type new-field-value)]
    (-> state
        (assoc-in [:log] new-log)
        (assoc-in [:changed?] true)
        (update-valid-status field-id valid?))))

(defn ^:export on-type-selection
  [state new-type]
  ;; TODO: VAlidate log-type
  (let [old-log (:log state)
        new-log (-> old-log
                    (assoc-in [:type] new-type)
                    (assoc-in [:data] (log.db/get-initial-data new-type)))
        new-log (assoc-in new-log [:value] (log.db/derive-log-text new-log))]
    (-> state
        (assoc-in [:log] new-log)
        (assoc-in [:changed?] true)
        (assoc-in [:invalid-fields] []))))

;; WM API

(defn ^:export popup-will-open
  [_ctx parent-id args xargs]
  [:open-popup :log-viewer :log-edit parent-id args xargs])

(defn ^:export popup-did-open
  [{wm-db :wm game-db :game} parent-id [server-cid log-id] xargs]
  (let [parent-window (wm.db/query wm-db parent-id)
        next-position (wm.db/query-calculate-next-position
                       wm-db parent-window default-length :popup)]
    [:ok (initial-state game-db server-cid log-id) (open-opts next-position)]))

(defn close-callback
  [parent-id app-id xargs]
  [:web|wm|perform
   [:close-popup :log-viewer :log-edit [parent-id app-id] [] xargs]])

(defn confirm-discard-changes
  [[parent-id app-id] xargs]
  [:confirm app-id
   {:text
    (str "There are unsaved log modifications. "
         "Are you sure you want to close this window?")
    :btn-1 {:text "Cancel"
            :class [:primary]
            :event [:web|wm|perform [:focus app-id]]}
    :btn-2 {:text "Yes, close"
            :class [:secondary]
            :event (close-callback parent-id app-id xargs)}}
   {:xargs :louco}])

(defn ^:export popup-will-close
  [_ctx [parent-id app-id] state args xargs]
  (if (has-changed? state)
    (confirm-discard-changes [parent-id app-id] xargs)
    [:close-popup :log-viewer :log-edit [parent-id app-id] args xargs]))

(defn ^:export popup-did-close
  [_ctx _family-ids _state _args _xargs]
  [:ok])

(defn ^:export popup-will-focus
  [{app-db :app} [parent-id app-id] _state _args _xargs]
  (let [query-app (apps.db/query app-db app-id)
        blocking-popup-id (apps.db/query-has-blocking-popups query-app)]
    (if-not blocking-popup-id
      [:focus app-id]
      [:vibrate-focus blocking-popup-id {:subfocus app-id}])))
(defn ^:export popup-did-focus
  [_ctx _family-ids _state _args _xargs]
  [:ok])
