(ns driver.sse.handlers
  (:require [com.yetanalytics.sse-fx.events :refer [register-all!]]
            [com.yetanalytics.sse-fx.event-source :as event-source]
            [he.core :as he]
            [driver.rest.request :as request]
            [game.db]))

(event-source/register!)

(defn get-uri
  [csrf-token]
  (str "https://localhost:4000/v1/subscribe?_csrf-token=" csrf-token))

(he/reg-event-fx
 :driver|sse|subscribe
 (fn [{gdb :db} _]
   (let [game-db (game.db/get-context gdb)
         csrf-token (game.db/get-csrf-token game-db)]
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

(defn keywordize-keys
  [entry]
  (cond
    (string? entry) entry

    (map? entry)
    (into {}
          (for [[k v] entry]
            [(keyword k)
             (if (or (map? v)
                     (vector? v))
               (keywordize-keys v)
               v)]))

    :else
    (into []
          (for [v entry]
            (keywordize-keys v)))))

(he/reg-event-fx
 :driver|sse|on-message
 (fn [_ [_ _ raw_msg]]
   (let [payload (keywordize-keys (get raw_msg "payload"))
         event (get payload :event :ping)]
     {:dispatch [:event|dispatcher event payload]})))

(he/reg-event-fx
 :driver|sse|on-ping
 (fn [_ _]
   (request/ping {:on-ok [:dev|null] :on-fail []})))

(he/reg-event-dummy :driver|sse|on-open)

(he/reg-event-fx
 :driver|sse|on-error
 (fn [{gdb :db} [_ _ error]]
   (println (str "SSE Error: " error))
   {:db gdb}))

(he/reg-event-db
 :driver|sse|on-close
 (fn [db _]
   (println "SSE Closed")
   db))

