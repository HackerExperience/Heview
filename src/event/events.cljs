(ns event.events
  (:require [taoensso.truss :as truss :refer-macros [have have! have?]]))

(defn v-event [v]
  {:post [(have? [:or vector? list?] v)]})

;; Special
(defn ^:export ping
  [payload]
  {:post [(v-event %)]}
  [:driver|sse|on-ping])

;; Game

;; Game > Account

(defn ^:export server-password-acquired
  [[_ account-id] data _]
  (cljs.pprint/pprint data)
  [:game|server|password-acquired data])

;; Game > Server > Log

(defn ^:export log-created
  [[_ server-cid] data _meta]
  {:post [(v-event %)]}
  [:game|server|log|on-log-created server-cid data])

;; Game > Server > Process

(defn ^:export top-recalcado
  [[_ server-cid] data _meta]
  [:game|server|process|on-top-recalcado server-cid data])
