(ns game.server.software.db.type
  (:require [cljs.core.match :refer-macros [match]]))

(def software-types
  "List of all software types - at the order they should be displayed on the
  FileExplorer app"
  ["cracker" "log_forger" "log_recover"])

(def meta-map
  {"cracker" {:icon "fa fa-edit"
              :software-name "Cracker"
              :module-meta {:one {:id :bruteforce
                                  :name "Bruteforce"
                                  :icon "fa fa-edit"}
                            :two {:id :overflow
                                  :name "Overflow"
                                  :icon "far fa-folder"}}}
   "log_forger" {:icon "fa fa-archive"
                 :software-name "Log Forger"
                 :module-meta {:one {:id :log_edit
                                     :name "Edit log"
                                     :icon "fas fa-tasks"}
                               :two {:id :log_create
                                     :name "Create log"
                                     :icon "fab fa-windows"}}}
   "log_recover" {:icon "fab fa-freebsd"
                  :software-name "Log Recover"
                  :module-meta {:one {:id :log_recover
                                      :name "Recover log"
                                      :icon "fas fa-tasks"}}}})

;; Model

(defn get-module-number
  [type module-id]
  (let [module-meta (get-in meta-map [type :module-meta])]
    (if (= module-id (get-in module-meta [:one :id]))
      :one
      :two)))

;; Client Meta

(defn build-meta
  [file custom]
  (merge
   {:display-name (str (:name file) "." (:extension file))}
   custom))

(defn generate-client-meta
  [file]
  (build-meta file (get meta-map (:type file))))
