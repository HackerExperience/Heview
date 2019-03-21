(ns web.hud.db
  (:require [web.apps.db :as apps.db]))

;; Context ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-context
  [global-db]
  (get-in global-db [:web :hud]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:web :hud] updated-local-db))

;; Model ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def initial-db
  {:launcher {:show-overlay? false
              :overlay-input ""
              :config apps.db/launcher-config}
   :connection-info {:notification-panel nil
                     :server-selector nil}})

;; Model > Launcher ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn launcher-open-overlay
  [db]
  (assoc-in db [:launcher :show-overlay?] true))

(defn launcher-close-overlay
  [db]
  (assoc-in db [:launcher :show-overlay?] false))

;; Model > Connection Info ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn ci-get-notification-panel
  [db]
  (get-in db [:connection-info :notification-panel]))

(defn ci-open-notification-panel
  [db identifier]
  (assoc-in db [:connection-info :notification-panel] identifier))

(defn ci-close-notification-panel
  [db]
  (assoc-in db [:connection-info :notification-panel] nil))

(defn ci-toggle-notification-panel
  [db identifier]
  (if (= (ci-get-notification-panel db) identifier)
    (ci-close-notification-panel db)
    (ci-open-notification-panel db identifier)))

(defn ci-get-server-selector
  [db]
  (get-in db [:connection-info :server-selector]))

(defn ci-open-server-selector
  [db identifier]
  (assoc-in db [:connection-info :server-selector] identifier))

(defn ci-close-server-selector
  [db]
  (assoc-in db [:connection-info :server-selector] nil))

(defn ci-toggle-server-selector
  [db identifier]
  (if (= (ci-get-server-selector db) identifier)
    (ci-close-server-selector db)
    (ci-open-server-selector db identifier)))

(defn ci-server-selector-reducer
  [group-name acc [server-id server]]
  (let [entry {:id server-id
               :group group-name}]
    (conj acc entry)))

(defn ci-server-selector-format-entries-gateway
  [servers-sp servers-mp]
  (let [reducer-sp (partial ci-server-selector-reducer "Single Player")
        reducer-mp (partial ci-server-selector-reducer "Multiplayer")
        entries-sp (reduce reducer-sp [] servers-sp)
        entries-mp (reduce reducer-mp [] servers-mp)]
    (into []
          (flatten (conj entries-mp entries-sp)))))

(defn ci-server-selector-format-entries-endpoint
  [entries]
  (println "Endp entries")
  (println entries)
  entries)


;; Bootstrap ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn bootstrap []
  initial-db)
