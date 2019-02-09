(ns game.server.log.db
  (:require [cljs.core.match :refer-macros [match]]
            [he.date]
            [he.utils]))

;; Context

(defn get-context
  [global-db server-cid]
  (get-in global-db [:game :server server-cid :log]))
(defn get-context-game
  [game-db server-cid]
  (get-in game-db [:server server-cid :log]))

(defn set-context
  [global-db server-cid updated-local-db]
  (assoc-in global-db [:game :server server-cid :log] updated-local-db))
(defn set-context-game
  [game-db server-cid updated-local-db]
  (assoc-in game-db [:server server-cid :log] updated-local-db))

;; Model

(def log-map
  {"file_download_gateway"
   {:fields {:file_name
             {:type :file-name
              :desc "File name"
              :help "Name of the file that was downloaded"
              :validator-help "File name may only contain azAZ09"}
             :ip
             {:type :ip
              :desc "IP Address"
              :help "IP from whom this file was downloaded"
              :validator-help "This IP address is invalid."
              }}}
   "local_login" {:fields {}}})

(def dropdown-map
  [{:id "file_download_gateway"
    :label "File Download (Gateway)"
    :group "File Operations"}
   {:id "local_login"
    :label "Local access"
    :group "Misc"}])

(defn get-log-fields
  [log-type]
  (get log-map log-type))

(defn get-log
  [db log-id]
  (get-in db [:entry log-id]))

(defn generate-text-local-login
  [data]
  {:text "localhost logged in"
   :html [:span
          [:span.lv-logtext.lv-logtext-localhost "localhost"]
          " logged in"]})

(defn generate-text-file-download-gateway
  [data]
  {:text (str
          "localhost downloaded file " (:file_name data) " from " (:ip data))
   :html [:span
          [:span.lv-logtext.lv-logtext-localhost "localhost"]
          " downloaded file "
          [:span.lv-logtext.lv-logtext-filename (:file_name data)]
          " from "
          [:span.lv-logtext.lv-logtext-remoteip (:ip data)]]})

(defn derive-log-text
  [{type :type data :data}]
  (match [type]
         ["local_login"] (generate-text-local-login data)
         ["file_download_gateway"] (generate-text-file-download-gateway data)
         :else {:text "I don't know her" :html [:span "Nope"]}))

(defn validate-file-name
  [value]
  (he.utils/valid-file-name? value))

(defn validate-ip
  [value]
  (he.utils/valid-ipv4? value))

(defn validate-log-value
  [field-type value]
  (match [field-type]
         [:file-name] (validate-file-name value)
         [:ip] (validate-ip value)))

;; TODO: This can be automated based on `log-map`
(def initial-data-local-login {})
(def initial-data-file-download-gateway
  {:ip "" :file_name ""})

(defn get-initial-data
  [type]
  (match [type]
         ["local_login"] initial-data-local-login
         ["file_download_gateway"] initial-data-file-download-gateway))

(defn format-log
  [log]
  (let [{timestamp :timestamp
         type :type
         data :data} log]
    {:datetime (he.date/timestamp-to-datetime timestamp)
     :type type
     :data data
     :value (derive-log-text log)}))

(defn add-new-log
  [db log]
  (let [log-id (he.utils/get-canonical-id (:log_id log))
        new-logs (conj (:entry db) {log-id (format-log log)})]
    (-> db
        (assoc-in [:entry] new-logs))))

;; Bootstrap

(defn bootstrap-log-reducer
  "Reduces the raw bootstrap response into the proper internal format.
  Removes the `log_id` information, since it's already present at the map key."
  [acc log]
  (let [log-id (he.utils/get-canonical-id (:log_id log))]
    (into
     (sorted-map-by >)
     (assoc acc log-id (format-log log)))))

(defn bootstrap-server
  [db data]
  (-> db
      (assoc-in [:log :entry] (reduce bootstrap-log-reducer {} data))))
