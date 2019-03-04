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
   {:modules (:modules file)
    :size (:display-size file)
    :license (:license file)}))

(defn- reducer-filter
  [filter-fn sort-fn acc [file-id file]]
  (let [file-type (:type file)
        type-entries (get acc file-type [])
        new-type-entries (sort-fn type-entries [file-id (format-display file)])]
    (assoc acc file-type new-type-entries)))

(defn- filter-fn
  [filter-config file]
  (if (nil? filter-config)
    false
    (do
      (println "Todo")
      false)))

(defn- index-module-reducer
  [module-number current-file index [_ list-file]]
  (let [module-id (get-in current-file [:module-meta module-number :id])
        current-version (get-in current-file [:modules module-id :version])
        file-version (get-in list-file [:modules module-id :version])]
    (if (<= current-version file-version)
      (inc index)
      (reduced index))))

(defn- sort-by-module
  [module-id acc [file-id file]]
  (let [index (reduce (partial index-module-reducer module-id file) 0 acc)]
    (he.utils/conj-at-pos index acc [file-id file])))

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
  [sort-config acc [file-id file]]
  (match (:sort-order sort-config)
         :module-one (sort-by-module :one acc [file-id file])
         :module-two (sort-by-module :two acc [file-id file])
         :az-asc (sort-alphabetically > acc [file-id file])
         :az-desc (sort-alphabetically < acc [file-id file])
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
  *grouped by their file types*. It is not a 'global' sort."
  [filter-config sort-config source-files]
  (let [partial-filter-fn (partial filter-fn filter-config)
        partial-sort-fn (partial sort-fn sort-config)]
    (-> (partial reducer-filter partial-filter-fn partial-sort-fn)
        (reduce {} source-files)
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

(defn ^:export will-open
  [_ctx app-context]
  [:open-app :file-explorer app-context])
(defn ^:export did-open
  [{game-db :game wm-db :wm} app-context]
  (let [server-cid (wm.db/get-server-cid wm-db app-context)
        software-db (software.db/get-context-game game-db server-cid)
        main-storage-id (software.db/get-main-storage-id software-db)]
    [:ok (initial main-storage-id) open-opts]))

(defn ^:export will-close
  [_ctx app-id _state _args]
  [:close-app app-id])
(defn ^:export did-close
  [_ctx _app-id _state _args]
  [:ok])

(defn ^:export will-focus
  [_ctx app-id _state _args]
  [:focus app-id])
(defn ^:export did-focus
  [_]
  [:ok])

;; Popup handlers

(defn ^:export popup-may-open
  [_ popup-type parent-id args]
  [:open-popup :file-explorer popup-type parent-id args])
(defn ^:export popup-may-close
  [_ctx popup-type family-ids _state args]
  [:close-popup :file-explorer popup-type family-ids args])
(defn ^:export popup-may-focus
  [_ctx popup-type family-ids _state args]
  [:focus-popup :file-explorer popup-type family-ids args])
