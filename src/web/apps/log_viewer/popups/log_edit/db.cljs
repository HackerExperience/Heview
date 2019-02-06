(ns web.apps.log-viewer.popups.log-edit.db
  (:require [web.apps.db :as apps.db]
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
  [server-cid log-id]
  {:server-cid server-cid
   :log-id log-id})

;; Public Interface

(defn ^:export popup-will-open
  [_ctx parent-id args xargs]
  [:open-popup :log-viewer :log-edit parent-id args xargs])

(defn ^:export popup-did-open
  [[_ wm-db] parent-id [server-cid log-id] xargs]
  (let [parent-window (wm.db/query wm-db parent-id)
        next-position (wm.db/query-calculate-next-position
                       wm-db parent-window default-length :popup)]
    [:ok (initial-state server-cid log-id) (open-opts next-position)]))

(defn close-callback
  [parent-id app-id xargs]
  [:web|wm|perform
   [:close-popup :log-viewer :log-edit [parent-id app-id] [] xargs]])

(defn ^:export popup-will-close
  [_ctx [parent-id app-id] _state _args xargs]
  [:confirm
   app-id
   {:text
    (str "There are unsaved log modifications. "
         "Are you sure you want to close this window?")
    :btn-1 {:text "Cancel"
            :class [:primary]
            :event [:web|wm|perform [:noop]]}
    :btn-2 {:text "Yes, close"
            :class [:secondary]
            :event (close-callback parent-id app-id xargs)}}
   {:xargs :louco}])

(defn ^:export popup-did-close
  [_ctx _family-ids _state _args _xargs]
  [:ok])

(defn ^:export popup-will-focus
  [[app-db _] [parent-id app-id] _state _args _xargs]
  (let [query-app (apps.db/query app-db app-id)
        blocking-popup-id (apps.db/query-has-blocking-popups query-app)]
    (if-not blocking-popup-id
      [:focus app-id]
      [:vibrate-focus blocking-popup-id {:subfocus app-id}])))
(defn ^:export popup-did-focus
  [_ctx _family-ids _state _args _xargs]
  [:ok])
