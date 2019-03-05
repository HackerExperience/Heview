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

(defn get-best-file-id
  [db type module-id]
  (let [type (name type)
        module-number (if (nil? module-id)
                        :one
                        (software.db.type/get-module-number type module-id))
        storage-id (get-main-storage-id db)]
    (nth (get-in db [storage-id :cache type module-number]) 0 nil)))

(defn get-best-file
  [db type module-id]
  (let [file-id (get-best-file-id db type module-id)
        storage-id (get-main-storage-id db)]
    [file-id (get-in db [storage-id :files file-id])]))

(defn search-file
  "You know the file id and the server ID, but not the storage ID."
  [db file-id]
  (get-in db [(get-main-storage-id db) :files file-id]))

;; Bootstrap

(defn sort-files-index-reducer
  [files module-number file-id index list-file-id]
  (let [file (get files file-id)
        list-file (get files list-file-id)
        module-id (get-in file [:client-meta :module-meta module-number :id])
        version (get-in file [:modules module-id :version])
        list-version (get-in list-file [:modules module-id :version])]
    (if (<= version list-version)
      (inc index)
      (reduced index))))

(defn sort-files
  [files module-id acc file-id]
  (let [reducer-fn (partial sort-files-index-reducer files module-id file-id)
        index (reduce reducer-fn 0 acc)]
    (he.utils/conj-at-pos index acc file-id)))

(defn bootstrap-cache-reducer
  [files acc [file-id file]]
  (let [file-type (:type file)
        has-two? (get-in file [:client-meta :module-meta :two])
        {one-entries :one
         two-entries :two} (get acc file-type {:one [] :two []})
        new-one-entries (sort-files files :one one-entries file-id)
        new-two-entries (if-not (nil? has-two?)
                          (sort-files files :two two-entries file-id)
                          [])]
    (assoc acc file-type {:one new-one-entries
                          :two new-two-entries})))

(defn bootstrap-cache-files
  "This method creates a cache of all files in the storage. It outputs a map
  with file type (cracker, log-recover, log-forger etc) as key, and as value a
  submap with `:one` and `:two` keys, each with a list sorted by primary and
  secondary modules versions (respectively).
  In other words: it sorts all files based on the primary and secondary modules
  versions. This result will be used e.g. by the FileExplorer to display the
  files in a sorted fashion, without having to re-sort the list every time."
  [files]
  (reduce (partial bootstrap-cache-reducer files) {} files))

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
                     :storage storage
                     :cache (bootstrap-cache-files files)}]
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
