(ns web.os.popups.confirm.db
  (:require [web.apps.db :as apps.db]
            [web.wm.db :as wm.db]))

(def default-length
  {:len-x 370
   :len-y 150})

(defn open-opts
  [next-position]
  {:len-x (:len-x default-length)
   :len-y (:len-y default-length)
   :x (:x next-position)
   :y (:y next-position)})

(defn initial-state
  [config]
  {:config config})

(defn ^:export popup-will-open
  [[app-db _] parent-id args xargs]
  (let [query-parent (apps.db/query app-db parent-id)
        blocking-popup-id (apps.db/query-has-blocking-popups query-parent)]
    (if-not blocking-popup-id
      [:open-popup :os :confirm parent-id args xargs]
      [:vibrate-focus blocking-popup-id])))

(defn ^:export popup-did-open
  [[_ wm-db] parent-id [config] xargs]
  (let [parent-window (wm.db/query wm-db parent-id)
        next-position (wm.db/query-calculate-next-position
                       wm-db parent-window default-length :popup)]
    [:ok (initial-state config) (open-opts next-position)]))

(defn ^:export popup-will-close
  [_ctx [parent-id _app-id] _state args xargs]
  [:close-popup :os :confirm [parent-id _app-id] args])
(defn ^:export popup-did-close
  [_ctx _family-ids _state _args _xargs]
  [:ok])

(defn ^:export popup-will-focus
  [_ctx [parent-id app-id] _state _args _xargs]
  [:focus app-id {:subfocus parent-id}])
(defn ^:export popup-did-focus
  [_ctx popup-type family-ids _state _args _xargs]
  [:ok])
