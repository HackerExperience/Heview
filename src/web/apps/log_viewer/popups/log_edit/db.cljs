(ns web.apps.log-viewer.popups.log-edit.db
  (:require [he.utils]
            [game.server.log.db :as log.db]
            [game.server.log.db.data :as log.db.data]
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
   :y (:y next-position)
   :config {:contextable false
            :icon-class "fas fa-pen"}})

;; Model

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

(defn get-server-cid
  [state]
  (:server-cid state))

(defn get-log-id
  [state]
  (:log-id state))

(defn get-log
  [state]
  (:log state))

;; Log Edit request

(defn log-edit-callback
  [app-id]
  {:on-ok [:web|apps|log-viewer|log-edit|log|edit|ok app-id]
   :on-fail [:web|apps|log-viewer|log-edit|log|edit|fail app-id]})

;; Public API

;; Events API

(defn ^:export on-field-change
  [state field-id field-type new-field-value]
  (let [old-log (:log state)
        new-log (assoc-in old-log [:data field-id] new-field-value)
        new-log-value (log.db.data/derive-log-text new-log)
        new-log (assoc-in new-log [:value] new-log-value)
        valid? (log.db.data/validate-log-value field-type new-field-value)]
    (-> state
        (assoc-in [:log] new-log)
        (assoc-in [:changed?] true)
        (update-valid-status field-id valid?))))

(defn ^:export on-type-selection
  [state new-type]
  ;; TODO: VAlidate log-type
  (let [old-log (:log state)
        new-log (as-> old-log $
                    (assoc-in $ [:type] new-type)
                    (assoc-in $ [:data] (log.db.data/get-initial-data new-type))
                    (assoc-in $ [:value] (log.db.data/derive-log-text $)))]
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
            :event (close-callback parent-id app-id xargs)}
    :title "Discard changes?"}])

(defn ^:export popup-will-close
  [_ctx [parent-id app-id] state args xargs]
  (if (has-changed? state)
    (confirm-discard-changes [parent-id app-id] xargs)
    [:close-popup :log-viewer :log-edit [parent-id app-id] args xargs]))

(defn ^:export popup-did-close
  [_ctx _family-ids _state _args _xargs]
  [:ok])

(defn ^:export popup-will-focus
  [{app-db :app} [_ app-id] _state _args _xargs]
  (let [query-app (apps.db/query app-db app-id)
        blocking-popup-id (apps.db/query-has-blocking-popups query-app)]
    (if-not blocking-popup-id
      [:focus app-id]
      [:vibrate-focus blocking-popup-id {:subfocus app-id}])))
(defn ^:export popup-did-focus
  [_ctx _family-ids _state _args _xargs]
  [:ok])
