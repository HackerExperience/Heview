(ns web.wm.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]))

(defn wm
  [db _]
  (:wm db))

;; WM ;;

(rf/reg-sub
 :web|wm|viewport
 :<- [:web|wm]
 (fn [db _]
   (:viewport db)))

(rf/reg-sub
 :web|wm|focused-window
 :<- [:web|wm]
 (fn [db _]
   (:focused-window db)))

;; Windows ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn with-window-data-callback
  [[_ app-id]]
  [(he/subscribed [:web|wm|window|data app-id])])

(def with-window-data
  #(with-window-data-callback %))

(rf/reg-sub
 :web|wm|has-window-moving?
 :<- [:web|wm]
 (fn [db _]
   (:window-moving? db)))

(rf/reg-sub
 :web|wm|windows
 :<- [:web|wm]
 (fn [db _]
   (:windows db)))

(rf/reg-sub
 :web|wm|window|data
 :<- [:web|wm|windows]
 (fn [db [_ app-id]]
   (get db app-id)))

(rf/reg-sub
 :web|wm|window|config
 with-window-data
 (fn [[window]]
   (:config window)))

(rf/reg-sub
 :web|wm|window|moving?
 :<- [:web|wm|windows]
 (fn [db [_ app-id]]
   (get-in db [app-id :moving?])))

(rf/reg-sub
 :web|wm|window|focused?
 :<- [:web|wm|windows]
 (fn [db [_ app-id]]
   (get-in db [app-id :focused?])))

(rf/reg-sub
 :web|wm|window|seq-id
 with-window-data
 (fn [[window]]
   (:seq-id window)))

(rf/reg-sub
 :web|wm|window|file-id
 with-window-data
 (fn [[window]]
   (:file-id window)))

(rf/reg-sub
 :web|wm|window|file-type
 with-window-data
 (fn [[window]]
   (:file-type window)))

;; Windows > Header ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(rf/reg-sub
 :web|wm|window|header|files
 (fn [[_ server-cid file-type]]
   [(he/subd [:game|server|software|files|type server-cid file-type])
    (he/subd [:game|server|software|files|cache|type server-cid file-type])])
 (fn [[files sorted]]
   (reduce (fn [acc file-id]
             (conj acc (merge (get files file-id)
                              {:id file-id}))) [] sorted)))

;; Session ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn with-session-data-callback
  [[_ session-id]]
  [(he/subscribed [:web|wm|session session-id])])

(def with-session-data
  #(with-session-data-callback %))

(rf/reg-sub
 :web|wm|active-session
 :<- [:web|wm]
 (fn [db _]
   (:active-session db)))

(rf/reg-sub
 :web|wm|current-session
 :<- [:web|wm|active-session]
 (fn [session-id]
   (he/subscribe [:web|wm|session session-id])))

(rf/reg-sub
 :web|wm|current-session|apps
 :<- [:web|wm|current-session]
 (fn [session]
   (:apps session)))

(rf/reg-sub
 :web|wm|sessions
 :<- [:web|wm]
 (fn [db _]
   (:sessions db)))

(rf/reg-sub
 :web|wm|session
 :<- [:web|wm|sessions]
 (fn [db [_ session-id]]
   (get db session-id)))

(rf/reg-sub
 :web|wm|session|apps
 with-session-data
 (fn [[session]]
   (:apps session)))

(rf/reg-sub
 :web|wm|session|context-cid
 with-session-data
 (fn [[session] [_ _ context]]
   (if (= context :local)
     (:gateway session)
     (:endpoint session))))
