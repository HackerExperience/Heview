(ns web.wm.windowable
  (:require [clojure.string :as str]
            [web.apps.dispatcher :as apps.dispatcher]))

(defn dispatch-open
  [app-type]
  (apps.dispatcher/dispatch-db app-type :on-open))

(defn dispatch-close
  [app-type app-state]
  (apps.dispatcher/dispatch-db app-type :on-close app-state))

;; (defmulti dispatch-open
;;   (fn [app-type] app-type))

;; (defmethod dispatch-open :log-viewer
;;   [_]
;;   (log-viewer.db/on-open))

;; (defmulti dispatch-close
;;   (fn [type state] type))

;; (defmethod dispatch-close :log-viewer
;;   [type state]
;;   (log-viewer.db/on-close state))

(defn open
  [app-type]
  (dispatch-open app-type))

(defn close
  [type state]
  (dispatch-close type state))

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


