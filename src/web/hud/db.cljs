(ns web.hud.db
  (:require [web.apps.db :as apps.db]))

;; Context ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-context
  [global-db]
  (get-in global-db [:web :hud]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:web :hud] updated-local-db))

;; Model ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def initial-db
  {:launcher {:show-overlay? false
              :overlay-input ""
              :config apps.db/launcher-config}
   :connection-info {:notification-panel nil}})

;; Model > Launcher ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn launcher-open-overlay
  [db]
  (assoc-in db [:launcher :show-overlay?] true))

(defn launcher-close-overlay
  [db]
  (assoc-in db [:launcher :show-overlay?] false))

;; Model > Connection Info ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn ci-get-notification-panel
  [db]
  (get-in db [:connection-info :notification-panel]))

(defn ci-open-notification-panel
  [db identifier]
  (assoc-in db [:connection-info :notification-panel] identifier))

(defn ci-close-notification-panel
  [db]
  (assoc-in db [:connection-info :notification-panel] nil))

(defn ci-toggle-notification-panel
  [db identifier]
  (if (= (ci-get-notification-panel db) identifier)
    (ci-close-notification-panel db)
    (ci-open-notification-panel db identifier)))

;; Bootstrap ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn bootstrap []
  initial-db)
