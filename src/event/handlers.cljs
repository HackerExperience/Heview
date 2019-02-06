(ns event.handlers
  (:require [clojure.string :as str]
            [he.core :as he]
            [he.dispatch]
            [event.events]))

(defn dash-event-name
  [event-name]
  (str/replace event-name #"_" "-"))

(defn get-domain-info
  [payload]
  [(keyword (get payload :domain)) (keyword (get payload :domain_id))])

(defn call-dispatcher
  [event-name payload]
  (let [dashed-event-name (dash-event-name event-name)
        domain-info (get-domain-info payload)
        {data :data meta :meta} payload]
    (he.dispatch/call
     (str "event.events/" dashed-event-name)
     [domain-info data meta])))

(he/reg-event-fx :event|dispatcher
                 (fn [{:keys [db]} [_ event-name payload]]
                   {:dispatch (call-dispatcher event-name payload)}))
