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
              :config apps.db/launcher-config}})

(defn launcher-open-overlay
  [db]
  (assoc-in db [:launcher :show-overlay?] true))

(defn launcher-close-overlay
  [db]
  (assoc-in db [:launcher :show-overlay?] false))

;; Bootstrap ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn bootstrap []
  initial-db)
