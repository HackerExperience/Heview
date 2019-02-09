(ns web.os.popups.os-error.db
  (:require [web.wm.db :as wm.db]))

(def default-length
  {:len-x 470
   :len-y 150})

(defn open-opts
  [viewport]
  {:len-x (:len-x default-length)
   :len-y (:len-y default-length)
   ;; Center of the screen
   :x (-
       (int (/ (:x viewport) 2))
       (int (/ (:len-x default-length) 2)))
   :y (-
       (int (/ (:y viewport) 2))
       (int (/ (:len-y default-length) 2)))
   :config {:full-view true}})

(defn initial-state
  [{reason :reason source :source}]
  {:reason reason
   :source source})

(defn ^:export popup-will-open
  [_ parent-id args xargs]
  [:open-popup :os :os-error parent-id args xargs])

(defn ^:export popup-did-open
  [{wm-db :wm} parent-id [error-data] _xargs]
  (let [viewport (wm.db/get-viewport wm-db)]
    [:ok (initial-state error-data) (open-opts viewport)]))

(defn ^:export popup-will-close
  [_ctx [parent-id app-id] _state args xargs]
  [:close-popup :os :os-error [parent-id app-id] args])
(defn ^:export popup-did-close
  [_ctx _family-ids _state _args _xargs]
  [:ok])

(defn ^:export popup-will-focus
  [_ctx [_parent-id app-id] _state _args _xargs]
  [:focus app-id])
(defn ^:export popup-did-focus
  [_ctx popup-type family-ids _state _args _xargs]
  [:ok])
