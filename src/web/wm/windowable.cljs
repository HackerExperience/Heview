(ns web.wm.windowable
  (:require [web.apps.log-viewer.db :as log-viewer.db]))

(defmulti dispatch-open
  (fn [app-type] app-type))

(defmethod dispatch-open :log-viewer
  [_]
  (log-viewer.db/on-open))

(defmulti dispatch-close
  (fn [type state] type))

(defmethod dispatch-close :log-viewer
  [type state]
  (log-viewer.db/on-close state))

(defn open
  [app-type]
  (dispatch-open app-type))

(defn close
  [{:keys [state meta]}]
  (dispatch-close (:type meta) state))

;; (defprotocol IWindowable
;;   (close [this]))

;; ;; Apps

;; (defrecord LogViewer [current-filter]
;;   IWindowable
;;   (close [this]
;;     (log-viewer.db/on-close this)))


;; (extend-protocol IWindowable
;;   LogViewer
;;   (close [this]
;;     (close (LogViewer. this))))


