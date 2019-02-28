(ns game.server.process.db.type
  (:require [cljs.core.match :refer-macros [match]]))

(defn build-meta
  [opts]
  {:action-str (:action opts)
   :icon-class (:icon opts)
   :tm-note (:tm-note opts)})

(defn meta-log-forge-edit
  [process]
  (build-meta
   {:action "Edit log"
    :icon "fa fa-edit"
    :tm-note [:span "Tomato.logf"]}))

(defn generate-client-meta
  [process]
  (match (:type process)
         "log_forge_edit" (meta-log-forge-edit process)
         else (he.error/runtime (str "Unknown process type: " else))))

