(ns game.server.db
  (:require [game.server.log.db :as log.db]
            [game.server.process.db :as process.db]
            [game.server.software.db :as software.db]))

;; Context ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-context
  [global-db]
  (get-in global-db [:game :server]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:game :server] updated-local-db))

;; Bootstrap ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn format-nip
  [[network-id ip]]
  {:network-id network-id
   :ip ip})

;; TODO: Move to utils
(defn nip->cid
  [network-id ip]
  (str ip "@" network-id))

(defn format-meta-player
  [data]
  (map
   (fn [x] (update x :type #(keyword %)))
   data))

(defn format-meta-remote
  [data]
  data)

(defn bootstrap-account-gateways-reducer
  [acc gateway-info]
  (assoc acc (:server_id gateway-info) {:endpoints (:endpoints gateway-info)}))

(defn bootstrap-account-endpoints-reducer
  [acc endpoint-info]
  (let [endpoint-nip (nip->cid (:network_id endpoint-info) (:ip endpoint-info))]
    (assoc acc endpoint-nip {:link endpoint-info})))

(defn format-meta
  [data]
  {:gateways (reduce bootstrap-account-gateways-reducer {} (:player data))
   :endpoints (reduce bootstrap-account-endpoints-reducer {} (:remote data))})

;; (defn bootstrap-account
;;   [db data]
;;   (-> db
;;       (assoc-in [:game :server :meta] (format-meta data))))

(defn bootstrap-account
  [data]
  (-> {}
      (assoc-in [:meta] (format-meta data))))

(defn initial-instance-meta
  [instance data]
  (assoc instance :meta {:hostname (:name data)
                         :nips (:nips data)}))

(defn initial-instance
  [data]
  (-> {}
      (initial-instance-meta data)))

(defn server-instance
  "Creates a state instance for one specific server."
  [data]
  (-> (initial-instance data)
      (log.db/bootstrap-server (:logs data))
      (process.db/bootstrap-server (:processes data))
      (software.db/bootstrap-server-storages (:storages data))
      (software.db/bootstrap-server-main-storage (:main_storage data))
      ))

(defn bootstrap-server-add-endpoints
  [db {endpoints :endpoints gateway-id :server_id}]
  (assoc-in db [gateway-id :endpoints] endpoints))

(defn bootstrap-server-add-link
  [db link-info]
  (let [endpoint-cid (nip->cid (:network_id link-info) (:ip link-info))]
    (println endpoint-cid)
    (assoc-in db [endpoint-cid :link] link-info)))

(defn bootstrap-server
  "Adds the server data (state instance) to the global app state (db).
  Notice it creates the instance at `server-instance` independently, i.e. free
  of any kind of context. Then it wraps the instance into the global state,
  effectively adding the context (i.e. the `server-cid`).
  The wrapping is done by the caller of this method (i.e. `game.db` itself)."
  [db data server-cid]
  (println "Bootstraping server")
  ;; (cljs.pprint/pprint data)
  (assoc-in db [(name server-cid)] (server-instance data)))

;; Model ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-gateways
  [db]
  (get-in db [:meta :gateways]))

(defn get-gateways-ids
  [db]
  (keys (get-gateways db)))

;; TODO (waiting backend support)
(defn get-mainframe
  [db]
  (first (get-gateways-ids db)))
