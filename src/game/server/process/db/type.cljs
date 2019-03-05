(ns game.server.process.db.type
  (:require [cljs.core.match :refer-macros [match]]))

(defn build-meta
  [opts]
  {:action-str (:action opts)
   :icon-class (:icon opts)
   :tm-note (:tm-note opts)})

(defn get-file-name
  [origin process]
  (let [file (get-in process [:data :file origin])]
    (str (:name file) "." (:extension file))))

(defn meta-cracker-bruteforce
  [process]
  (build-meta
   {:action "Bruteforce password"
    :icon "fa fa-edit"
    :tm-note [:span (get-file-name :source process)]}))

(defn meta-file-download
  [process]
  (build-meta
   {:action "Download file"
    :icon "fas fa-download"
    :tm-note [:span (get-file-name :target process)]}))

(defn meta-file-upload
  [process]
  (build-meta
   {:action "Upload file"
    :icon "fas fa-upload"
    :tm-note [:span (get-file-name :target process)]}))

(defn meta-log-forge-edit
  [process]
  (build-meta
   {:action "Edit log"
    :icon "fa fa-edit"
    :tm-note [:span "Tomato.logf"]}))

(defn generate-client-meta
  [process]
  (match (:type process)
         "cracker_bruteforce" (meta-cracker-bruteforce process)
         "file_download" (meta-file-download process)
         "file_upload" (meta-file-upload process)
         "log_forge_edit" (meta-log-forge-edit process)
         else (he.error/runtime (str "Unknown process type: " else))))

