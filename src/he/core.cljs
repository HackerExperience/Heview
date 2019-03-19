(ns he.core
  (:require [re-frame.core :as rf]
            [he.inject]))

(defn inject-sub
  [sub]
  (rf/inject-cofx ::he.inject/sub sub))

(defn reg-event-fx
  ([id handler]
   (rf/reg-event-fx id handler))
  ([id interceptor handler]
   (rf/reg-event-fx id interceptor handler)))

(defn reg-event-db
  ([id handler]
   (rf/reg-event-db id handler))
  ([id interceptor handler]
   (rf/reg-event-db id interceptor handler)))

(defn reg-event-dummy
  [id]
  (rf/reg-event-fx id [] (fn [_ _] {})))

(defn subscribe
  [id]
  @(rf/subscribe id))

(defn sub
  "Short for `subscribe`"
  [id]
  (subscribe id))

(defn subscribed
  [id]
  (rf/subscribe id))

(defn subd
  "Short for `subscribed`"
  [id]
  (subscribed id))


;; TODO: Can't make this to work right now
;; (defn reg-sub
;;   ([id fun]
;;    (rf/reg-sub id fun))
;;   ([id & args]
;;    (rf/reg-sub id args)
;;    (apply rf/reg-sub [id args])))

(defn dispatch
  [event]
  (rf/dispatch event))

(defn dis
  "Short for `dispatch`"
  [event]
  (dispatch event))

(defn dispatch-sync
  [event]
  (rf/dispatch-sync event))

(defn tip-down
  [content]
  [:div.tip
   [:span.tip-down content]])

(defn with-app-state-callback
  [[_ app-id]]
  [(rf/subscribe [:web|apps|state app-id])])
(def with-app-state
  #(with-app-state-callback %))

(defn with-game-server-data-callback
  [[_ server-cid]]
  [(rf/subscribe [:game|server|data server-cid])])
(def with-game-server-data
  #(with-game-server-data-callback %))
