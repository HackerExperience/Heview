(ns event.handlers
  (:require [clojure.string :as str]
            [cljs.core.match :refer-macros [match]]
            [he.core :as he]
            [he.dispatch]
            [event.events]))

(defn call-event-handler
  [event-name args]
  (he.dispatch/call (str "event.events/" event-name) args))

(defn dash-event-name
  [event-name]
  (str/replace event-name #"_" "-"))

(defn get-domain-info
  [payload]
  [(keyword (:domain payload)) (:domain_id payload)])

(defn dispatcher-special
  [event-name payload]
  (match event-name
         :ping (call-event-handler "ping" [payload])
         other (he.error/runtime (str "Unknow event " event-name))))

(defn dispatcher-game
  [event-name payload]
  (let [dashed-event-name (dash-event-name event-name)
        domain-info (get-domain-info payload)
        {data :data meta :meta} payload]
    (call-event-handler dashed-event-name [domain-info data meta])))

(defn get-dispatcher-handler
  [event-name]
  (if (= event-name :ping)
    (partial dispatcher-special)
    (partial dispatcher-game)))

(defn call-dispatcher
  [event-name payload]
  (let [dispatcher-handler (get-dispatcher-handler event-name)
        dispatch-targets (dispatcher-handler event-name payload)]
    (if (vector? dispatch-targets)
      (list dispatch-targets)
      dispatch-targets)))

(he/reg-event-fx :event|dispatcher
                 (fn [{:keys [db]} [_ event-name payload]]
                   {:dispatch-n (call-dispatcher event-name payload)}))
