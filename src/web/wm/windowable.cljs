(ns web.wm.windowable
  (:require [clojure.string :as str]
            [web.apps.dispatcher :as apps.dispatcher]))

(defn dispatch-db
  [method type & args]
  (apps.dispatcher/dispatch-db type method args))

(defn open
  [type]
  (dispatch-db :on-open type))

(defn close
  [type state]
  (dispatch-db :on-close type state))

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


