(ns game.server.software.db.type
  (:require [cljs.core.match :refer-macros [match]]))

(def software-types
  "List of all software types - at the order they should be displayed on the
  FileExplorer app"
  ["cracker" "log_forger" "log_recover"])

(defn build-meta
  [file custom]
  (merge
   {:display-name (str (:name file) "." (:extension file))}
   custom))

(defn meta-cracker
  [file]
  (build-meta
   file
   {:icon "fa fa-edit"
    :software-name "Cracker"
    :module-meta {:one {:id :bruteforce
                        :name "Bruteforce"
                        :icon "fa fa-edit"}
                  :two {:id :overflow
                        :name "Overflow"
                        :icon "far fa-folder"}}}))

(defn meta-log_forger
  [file]
  (build-meta
   file
   {:icon "fa fa-archive"
    :software-name "Log Forger"
    :module-meta {:one {:id :log_edit
                        :name "Edit log"
                        :icon "fas fa-tasks"}
                  :two {:id :log_create
                        :name "Create log"
                        :icon "fab fa-windows"}}}))

(defn meta-log_recover
  [file]
  (build-meta
   file
   {:icon "fab fa-freebsd"
    :software-name "Log Recover"
    :module-meta {:one {:id :log_recover
                        :name "Recover log"
                        :icon "fas fa-tasks"}}}))



(defn generate-client-meta
  [software]
  (match (:type software)
         "cracker" (meta-cracker software)
         "log_forger" (meta-log_forger software)
         "log_recover" (meta-log_recover software)
         else (do
                (he.error/match "Invalid software type" else)
                {})))
