(ns game.server.software.db
  (:require [he.utils]
            [he.utils.str :as he.str]
            [game.server.software.db.type :as software.db.type]))

;; Context

(defn get-context
  [global-db server-cid]
  (get-in global-db [:game :server server-cid :software]))
(defn get-context-game
  [game-db server-cid]
  (get-in game-db [:server server-cid :software]))

(defn set-context
  [global-db server-cid updated-local-db]
  (assoc-in global-db [:game :server server-cid :software] updated-local-db))

;; Model

(defn- format-version
  [version]
  (he.str/format "%.2f" (/ version 100)))

(defn- format-size
  [size]
  (let [[div formatter unit] (cond
                               (<= size 10000) [1000 "%.2f" "MB"]
                               (<= size 100000) [1000 "%.1f" "MB"]
                               (<= size 1000000) [1000 "%.0f" "MB"]
                               (<= size 10000000) [1000000 "%.0f" "GB"])]
    (str (he.str/format formatter (/ size div)) " " unit)))

(defn- build-file-modules-reducer
  [modules acc module-name]
  (let [version (get-in modules [module-name :version])]
    (assoc acc module-name {:version version
                            :display-version (format-version version)})))

(defn build-file-modules
  [modules]
  (reduce (partial build-file-modules-reducer modules) {} (keys modules)))

(defn build-file
  [file]
  {:type (:type file)
   :name (:name file)
   :extension (:extension file)
   :modules (build-file-modules (:modules file))
   :size (:size file)
   :display-size (format-size (:size file))
   :license "GPL"})

(defn format-file
  [raw-file]
  (let [file (build-file raw-file)
        client-meta (software.db.type/generate-client-meta file)]
    (assoc file :client-meta client-meta)))

(defn build-storage
  [storage-name]
  {:name storage-name})

(defn get-main-storage-id
  [db]
  (:main-storage-id db))

;; Bootstrap

(defn file-reducer
  [acc file]
  (assoc acc (he.utils/get-canonical-id (:id file)) (format-file file)))

(defn reduce-storage-data
  [[acc-files acc-fs] [path path-files]]
  (let [files (reduce file-reducer {} path-files)
        new-acc-files (merge acc-files files)
        ;; TODO: Make `acc-fs` (the `filesystem` submap) AZ ordered.
        new-acc-fs (assoc acc-fs path (vec (keys files)))]
    [new-acc-files new-acc-fs]))

(defn bootstrap-server-reducer
  [acc [storage-id storage-data]]
  (let [raw-filesystem (:filesystem storage-data)
        [files filesystem] (reduce reduce-storage-data [{} {}] raw-filesystem)
        storage (build-storage (:name storage-data))
        instance-db {:files files
                     :filesystem filesystem
                     :storage storage}]
    (merge acc
           (hash-map (he.utils/get-canonical-id storage-id) instance-db))))

(defn bootstrap-server-storages
  [server-db data]
  (let [software-db (reduce bootstrap-server-reducer {} data)]
    (assoc server-db :software software-db)))

(defn bootstrap-server-main-storage
  [server-db main-storage-id]
  (let [nice-main-storage-id (he.utils/get-canonical-id main-storage-id)]
    (assoc-in server-db [:software :main-storage-id] nice-main-storage-id)))
