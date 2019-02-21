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

(defn ^:export log-created
  [[_ server-cid] data _meta]
  {:post [(v-event %)]}
  [:game|server|log|on-log-created server-cid data])
