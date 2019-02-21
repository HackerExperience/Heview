(ns web.os.db
  (:require [web.os.popups.db]))

;; Context

(defn get-context
  [global-db]
  (get-in global-db [:web :os]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:web :os] updated-local-db))

;; Model

(defn init-os
  [db]
  (-> db
      (assoc :open-errors [])))

(defn has-os-error?
  [db {reason :reason}]
  (some #(= reason %) (:open-errors db)))

(defn add-os-error
  [db {reason :reason}]
  (update db :open-errors #(conj % reason)))

(defn remove-os-error
  [db reason]
  (update db :open-errors #(he.utils/vec-remove % (.indexOf % reason))))

;; Bootstrap

(defn bootstrap
  [db]
  (init-os db))
