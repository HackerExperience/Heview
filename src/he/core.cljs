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

(defn dispatch-sync
  [event]
  (rf/dispatch-sync event))
