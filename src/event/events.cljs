(ns event.events
  (:require [he.core :as he]))

(defn ^:export log-created
  [[_ server-cid] data _meta]
  [:game|server|log|on-log-created server-cid data])

