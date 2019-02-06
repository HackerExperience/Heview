(ns driver.sse.handlers
  (:require [com.yetanalytics.sse-fx.events :refer [register-all!]]
            [com.yetanalytics.sse-fx.event-source :as event-source]
            [he.core :as he]
            [game.db]))

(event-source/register!)

(defn get-uri
  [csrf-token]
  (str "https://localhost:4000/v1/subscribe?_csrf-token=" csrf-token))

(he/reg-event-fx
 :driver|sse|subscribe
 (fn [{:keys [db]} _]
   (let [csrf-token (game.db/get-csrf-token db)]
     {::event-source/init
      {:uri (get-uri csrf-token)
       :handle-open [:driver|sse|on-open]
       :handle-message [:driver|sse|on-message]
       :handle-error [:driver|sse|on-error]
       :handle-close [:driver|sse|on-close]
       :event-source-args {:config {:withCredentials true}}}})))

(he/reg-event-fx
 :driver|sse|close
 (fn [_ _]
   {::event-source/close
    {:keys :all}}))

(defn ping-dispatcher []
  [:driver|rest|request "GET" "ping" :simple {} {:on-ok [] :on-error []}])

(defn event-dispatcher
  [event payload]
  [:event|dispatcher event payload])

(defn parse-payload
  [payload]
  (let [event (get payload :event :ping)]
    (if (= event :ping)
      (ping-dispatcher)
      (event-dispatcher event payload))))

(defn keywordize-keys
  [map]
  (into {}
        (for [[k v] map]
          [(keyword k)
           (if (map? v)
             (keywordize-keys v)
             v)])))

(he/reg-event-fx
 :driver|sse|on-message
 (fn [db [_ _ raw_msg]]
   (let [payload (keywordize-keys (get raw_msg "payload"))
         dispatcher (parse-payload payload)]
     {:dispatch dispatcher})))

(he/reg-event-dummy :driver|sse|on-open)

(he/reg-event-fx
 :driver|sse|on-error
 (fn [db [_ _ error]]
   (println (str "SSE Error: " error))
   {:dispatch
    [:driver|rest|request "GET" "ping" :simple {} {:on-ok [] :on-error []}]}))

(he/reg-event-db
 :driver|sse|on-close
 (fn [db _]
   (println "SSE Closed")
   db))

