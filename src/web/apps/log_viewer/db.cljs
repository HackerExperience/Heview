(ns web.apps.log-viewer.db
  (:require [cljs.core.match :refer-macros [match]]
            [he.date]))

(def open-opts
  {:len-x 530
   :len-y 400})

(defn initial
  []
  {:selected nil})

(def log-viewer-datetime-formatter
  (he.date/formatter "yyyy-MM-dd HH:mm:ss"))

(defn format-log
  [[log_id log]]
  (let [{datetime :datetime
         {html :html} :value} log]
    [log_id
     {:date (he.date/format datetime log-viewer-datetime-formatter)
      :html html}]))

(defn get-selected-id
  [state]
  (get-in state [:selected]))

(defn select-log
  [state log-id]
  (assoc-in state [:selected] log-id))

(defn deselect-log
  [state]
  (assoc-in state[:selected] nil))

;; Interface

;; Events API

(defn ^:export on-click
  [state log-id]
  (let [selected-id (get-selected-id state)]
    (if (= selected-id log-id)
      (deselect-log state)
      (select-log state log-id))))

;; WM API

(defn ^:export will-open
  [_ctx]
  [:open-app :log-viewer])
(defn ^:export did-open
  [_ctx]
  [:ok (initial) open-opts])

(defn ^:export will-close
  [_ctx app-id _state _args]
  [:close-app app-id])
(defn ^:export did-close
  [_ctx _app-id _state _args]
  [:ok])

(defn ^:export will-focus
  [_ctx app-id _state _args]
  [:focus app-id {:meu :xargs}])
(defn ^:export did-focus
  [_]
  [:ok])

;; Popup handlers

(defn log-edit-may-open
  [ctx parent-id args]
  [:open-popup :log-viewer :log-edit parent-id args {:xargs :doido}])

(defn ^:export popup-may-open
  [ctx popup-type parent-id args]
  (match [popup-type]
         [:log-edit] (log-edit-may-open ctx parent-id args)
         [_] [:open-popup :log-viewer popup-type parent-id args]))
(defn ^:export popup-may-close
  [_ctx popup-type family-ids _state args]
  [:close-popup :log-viewer popup-type family-ids args {:xargs :noix}])
(defn ^:export popup-may-focus
  [_ctx popup-type family-ids _state args]
  [:focus-popup :log-viewer popup-type family-ids args {:xargs :novo}])
