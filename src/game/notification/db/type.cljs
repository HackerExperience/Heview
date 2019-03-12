(ns game.notification.db.type
  (:require [cljs.core.match :refer-macros [match]]))

(defn file-downloaded-meta
  [notif]
  (let [data (:data notif)
        file-name (str (:name data) "." (:extension data))]
    {:action "File downloaded"
     :targets [{:text file-name
                :icon "fa fa-file"}]
     :icon "fa fa-download"}))

(defn file-uploaded-meta
  [notif]
  (let [data (:data notif)
        file-name (str (:name data) "." (:extension data))]
    {:action "File uploaded"
     :targets [{:text file-name
                :icon "fa fa-file"}]
     :icon "fa fa-upload"}))

(defn log-revised-meta
  [notif]
  {:action "Log revised"
   :targets []
   :icon "fa fa-edit"})

(defn server-password-acquired-meta
  [notif]
  {:action "Server password acquired."
   :targets []
   :icon "fa fa-edit"})
(defn server-password-acquired-data
  [data]
  {:ip (:ip data)
   :network-id (:network_id data)
   :password (:password data)})

(defn base-data
  [data]
  data)
(defn base-meta
  [notif]
  (println "Unhandled notification: " (:code notif))
  {:action "Unhandled notification"
   :targets []
   :icon "far fa-question-circle"})

(defn get-data
  [code data]
  (match code
         "server_password_acquired" (server-password-acquired-data data)
         _ (base-data data)))

(defn get-dynamic-meta
  [notif]
  (match (:code notif)
         "file_downloaded" (file-downloaded-meta notif)
         "file_uploaded" (file-uploaded-meta notif)
         "log_revised" (log-revised-meta notif)
         "server_password_acquired" (server-password-acquired-meta notif)
         _ (base-meta notif)))
