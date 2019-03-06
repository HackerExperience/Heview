(ns web.os.popups.confirm.db
  (:require [web.apps.db :as apps.db]
            [web.wm.db :as wm.db]))

(def default-length
  {:len-x 370
   :len-y 150})

(defn open-opts
  [next-position config]
  {:len-x (:len-x default-length)
   :len-y (:len-y default-length)
   :x (:x next-position)
   :y (:y next-position)
   :config {:show-context false
            :show-minimize false
            :show-taskbar false
            :title (get config :title "Warning!")
            :icon-class "fas fa-skull-crossbones"}})

(defn initial-state
  [config]
  {:config config})

(defn ^:export popup-will-open
  [{app-db :app} parent-id args xargs]
  (let [query-parent (apps.db/query app-db parent-id)
        blocking-popup-id (apps.db/query-has-blocking-popups query-parent)]
    (if-not blocking-popup-id
      [:open-popup :os :confirm parent-id args xargs]
      [:vibrate-focus blocking-popup-id])))

(defn ^:export popup-did-open
  [{wm-db :wm} parent-id [config] xargs]
  (let [parent-window (wm.db/query wm-db parent-id)
        next-position (wm.db/query-calculate-next-position
                       wm-db parent-window default-length :popup)]
    [:ok (initial-state config) (open-opts next-position config)]))

(defn ^:export popup-will-close
  [_ctx [parent-id _app-id] _state args xargs]
  (list [:close-popup :os :confirm [parent-id _app-id] args]
        [:focus parent-id]))
(defn ^:export popup-did-close
  [_ctx _family-ids _state _args _xargs]
  [:ok])

(defn ^:export popup-will-focus
  [_ctx [parent-id app-id] _state _args _xargs]
  [:focus app-id {:subfocus parent-id}])
(defn ^:export popup-did-focus
  [_ctx popup-type family-ids _state _args _xargs]
  [:ok])
