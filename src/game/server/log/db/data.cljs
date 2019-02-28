(ns game.server.log.db.data
  (:require [cljs.core.match :refer-macros [match]]
            [he.utils]))

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
   "local_login" {:fields {}}
   "remote_login_gateway"
   {:fields {:ip
             {:type :ip
              :desc "IP Address"
              :help "IP of the server this gateway logged into"
              :validator-help "This IP address is invalid"}}}
   "remote_login_endpoint"
   {:fields {:ip
             {:type :ip
              :desc "IP Address"
              :help "IP of the server that logged into this gateway"
              :validator-help "This IP address is invalid"}}}})

(def dropdown-map
  [{:id "file_download_gateway"
    :label "File Download (Gateway)"
    :group "File Operations"}
   {:id "local_login"
    :label "Local access"
    :group "Misc"}
   {:id "remote_login_gateway"
    :label "Remote access (Gateway)"
    :group "Misc"}
   {:id "remote_login_endpoint"
    :label "Remote access (Endpoint)"
    :group "Misc"}])

(defn generate-text-local-login
  [data]
  {:text "localhost logged in"
   :html [:span
          [:span.lv-logtext.lv-logtext-localhost "localhost"]
          " logged in"]})

(defn generate-text-remote-login-gateway
  [{ip :ip}]
  {:text (str "localhost logged into " ip " as root.")
   :html [:span
          [:span.lv-logtext.lv-logtext-localhost "localhost"]
          " logged into "
          [:span.lv-logtext.lv-logtext-remoteip ip]
          " as root"]})

(defn generate-text-remote-login-endpoint
  [{ip :ip}]
  {:text (str ip " logged into localhost as root")
   :html [:span
          [:span.lv-logtext.lv-logtext-remoteip ip]
          " logged into "
          [:span.lv-logtext.lv-logtext-localhost "localhost"]
          " as root"]})

(defn generate-text-file-download-gateway
  [{file-name :file_name ip :ip}]
  {:text (str
          "localhost downloaded file " file-name " from " ip)
   :html [:span
          [:span.lv-logtext.lv-logtext-localhost "localhost"]
          " downloaded file "
          [:span.lv-logtext.lv-logtext-filename file-name]
          " from "
          [:span.lv-logtext.lv-logtext-remoteip ip]]})

(defn derive-log-text
  [{type :type data :data}]
  (match [type]
         ["local_login"] (generate-text-local-login data)
         ["remote_login_gateway"] (generate-text-remote-login-gateway data)
         ["remote_login_endpoint"] (generate-text-remote-login-endpoint data)
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
  {:ip "" :file_name "" :network_id "::"})
(def initial-data-remote-login-gateway
  {:ip "" :network_id "::"})
(def initial-data-remote-login-endpoint
  {:ip "" :network_id "::"})

(defn get-initial-data
  [type]
  (match [type]
         ["local_login"] initial-data-local-login
         ["remote_login_gateway"] initial-data-remote-login-gateway
         ["remote_login_endpoint"] initial-data-remote-login-endpoint
         ["file_download_gateway"] initial-data-file-download-gateway))

