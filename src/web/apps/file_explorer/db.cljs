(ns web.apps.file-explorer.db
  (:require [cljs.core.match :refer-macros [match]]
            [game.server.software.db :as software.db]
            [game.server.software.db.type :as software.db.type]
            [web.wm.db :as wm.db]))

(def open-opts
  {:len-x 530
   :len-y 400
   :config {:icon-class "far fa-folder"
            :title "File Explorer"}})

;; Model

(defn initial
  [main-storage-id]
  {:selected-file nil
   :selected-storage main-storage-id
   :view-mode :flat
   :sort-config {:sort-order :module-one}
   :filter-config nil})

(defn get-selected-file
  [db]
  (:selected-file db))

(defn deselect-file
  [db]
  (assoc db :selected-file nil))

(defn select-file
  [db file-id]
  (assoc db :selected-file file-id))

;; Interface

;; Sub API

(defn format-display
  [file]
  (merge
   (:client-meta file)
   {:type (:type file)
    :modules (:modules file)
    :size (:display-size file)
    :license (:license file)}))

(defn- filter-fn
  [filter-config file]
  (if (nil? filter-config)
    false
    (do
      (println "Todo")
      false)))

(defn- index-alphabet-reducer
  [compare-fn current-file index [_ list-file]]
  (if (compare-fn (:display-name current-file) (:display-name list-file))
    (inc index)
    (reduced index)))

(defn- sort-alphabetically
  [compare-fn acc [file-id file]]
  (let [index (reduce (partial index-alphabet-reducer compare-fn file) 0 acc)]
    (he.utils/conj-at-pos index acc [file-id file])))

(defn- sort-fn
  "Modules are already sorted, since the file input is the one from the software
  cache. On the other hand, if the user wants to sort files alphabetically, we
  have to sort here, hence the reducer."
  [files sort-config {entries-one :one entries-two :two}]
  (match (:sort-order sort-config)
         :module-one entries-one
         :module-two (if (empty? entries-two)
                       entries-one
                       entries-two)
         :az-asc (reduce (partial sort-alphabetically >) [] entries-one)
         :az-desc (reduce (partial sort-alphabetically <) [] entries-one)
         else (he.error/match "Sort fn" else)))

(defn- post-sort-fn-reducer
  [files-map sorted-files software-type]
  (conj sorted-files (get files-map software-type [])))

(defn- post-sort-fn
  [files-map]
  (let [reducer-fn (partial post-sort-fn-reducer files-map)
        files (reduce reducer-fn [] software.db.type/software-types)]
    ;; Flatten outtermost level only
    (apply concat files)))

(defn reducer-filter
  [files filter-fn sort-fn files-map [type entries]]
  (let [new-entries (sort-fn entries)]
    (assoc files-map type new-entries)))

(defn map-entries
  [files entries]
  (map (fn [file-id] [file-id (format-display (get files file-id))]) entries))

(defn map-cache-reducer
  [files new-cache [type {entries-one :one entries-two :two}]]
  (merge new-cache
         (hash-map type {:one (map-entries files entries-one)
                         :two (map-entries files entries-two)})))

(defn map-cache
  "By default, the cache only contains file IDs. Here, we replace these IDs with
  [File.ID File.t]. That's all we do, while keeping the same cache structure (a
  map with `$software-type` as key and the `:one`/`:two` submap.)"
  [files cache]
  (reduce (partial map-cache-reducer files) {} cache))

(defn filter-files
  "Filters and sorts the files that will be shown at the File Explorer app.
  This function is called any time the underlying filter config, sort config or
  the source files are modified.
  The first pass, at `reducer-filter`, will create a map of all files, with the
  underlying file type as key, and a vector of all files of that type as value.
  It first makes sure the file should be displayed by using the `filter-fn`. It
  then proceeds to sort the vector of files using the `sort-fn`.
  At the end of the reduction, we have a map with all software types and the
  underlying files already sorted. It then calls `post-sort-fn` to translate
  this map into a vector of all files, sorted and ready to display to the user.
  This final step is required because there exists an implicit sort function,
  which is: all files of the same type are grouped together. And any custom sort
  configurations must obey to this implicit rule. In other words: even if the
  user wants to sort files alphabetically, they will be sorted alphabetically
  *grouped by their file types*. It is not a 'global' sort.
  By default, the `files-cache` map already contains sorted lists of modules.
  That's why there's no sorting of modules here; they are already sorted at the
  cache level."
  [filter-config sort-config files-cache source-files]
  (let [files-cache (map-cache source-files files-cache)
        partial-sort-fn (partial sort-fn source-files sort-config)]
    (-> (partial reducer-filter source-files nil partial-sort-fn)
        (reduce {} files-cache)
        (post-sort-fn))))

;; Events API

(defn on-click-sort
  [db sort-id]
  (assoc-in db [:sort-config :sort-order] sort-id))

(defn on-click-file
  [db file-id]
  (if (= (get-selected-file db) file-id)
    (deselect-file db)
    (select-file db file-id)))

;; WM API

(defn ^:export did-open
  [{game-db :game wm-db :wm} app-context]
  (let [server-cid (wm.db/get-server-cid wm-db app-context)
        software-db (software.db/get-context-game game-db server-cid)
        main-storage-id (software.db/get-main-storage-id software-db)]
    [:ok (initial main-storage-id) open-opts]))
