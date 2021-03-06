(ns driver.rest.request
  (:require [clojure.string :as str]
            [driver.rest.validators :as v]))

(defn format-cid
  [server-cid]
  (-> server-cid
      (str/replace #":" ",")
      (str/replace #"@" "$")))

(defn format-nip
  [network-id ip]
  (str ip "$*"))

(defn build-params
  [method path body custom]
  (v/v v/request-build-params [method path body custom])
  (let [request-config (merge
                        {:method (name method) :path path :body body}
                        custom)]
    {:dispatch [:driver|rest|request request-config]}))

(defn callback-event
  ([callback status response]
   (callback-event callback status response {}))
  ([[callback & xargs] status response gargs]
   (v/request-callback callback)
   (v/request-status status)
   (v/request-callback-gargs gargs)
   [callback status response gargs (into [] xargs)]))

(defn wrap-callback
  ([callback status response]
   (wrap-callback callback status response {}))
  ([callback status response gargs]
   {:dispatch (callback-event callback status response gargs)}))

;; (defn wrap-callback-n
;;   [[callback & xargs] status response gargs ])

;; Session

(defn check-session
  [custom]
  (build-params :get "check-session" {} custom))

(defn sync
  [custom]
  (build-params :post "sync" {:client "web1"} custom))

(defn subscribe
  [custom]
  (build-params :post "subscribe" {} custom))

(defn ping
  [custom]
  (build-params :get "ping" {} custom))

;; Account

(defn account-login
  [username password custom]
  (v/v-each v/request-arg-string username password)
  (build-params :post "login" {:username username :password password} custom))

;; Game

(defn browse
  [server-cid address custom]
  (build-params
   :get
   (str "server/" (format-cid server-cid) "/browse?address=" address)
   {}
   custom))

;; Game > Log

(defn log-forge-edit
  [server-cid [log-id log] custom]
  (build-params
   :post
   (str "server/" (format-cid server-cid) "/log/" (format-cid log-id) "/edit")
   {:log_type (:type log)
    :log_data (:data log)}
   custom))

;; Game > Software

(defn software-file-download
  [target-nip file-id custom]
  (build-params
   :post
   (str "endpoint/" (format-cid target-nip)
        "/file/" (format-cid file-id) "/download")
   {}
   custom))

(defn software-file-upload
  [target-nip file-id custom]
  (build-params
   :post
   (str "endpoint/" (format-cid target-nip)
        "/file/" (format-cid file-id) "/upload")
   {}
   custom))

(defn software-bruteforce
  [gateway-id target-ip custom]
  (build-params
   :post
   (str "gateway/" (format-cid gateway-id)
        "/bruteforce/" (format-nip "::" target-ip))
   {}
   custom))

;; Game > Notification

(defn notification-read-all
  [body custom]
  (build-params
   :post
   "notification/read/all"
   body
   custom))

;; Game > Story

(defn story-reply
  [contact-id reply-id custom]
  (build-params
   :post
   "storyline/reply"
   {:contact_id contact-id
    :reply_id reply-id}
   custom))

;;

(defn server-login
  [server-cid ip password custom]
   (build-params
    :post
    (str "endpoint/" (format-nip "::" ip) "/login")
    {:password password
     :gateway_id server-cid}
    custom))

(defn logout
  [custom]
  (build-params :post "logout" {} custom))

;; Public / Extraneous

(defn install-setup-register-submit
  [body custom]
  (build-params :post "account/register" body custom))

(defn install-setup-register-check-username
  [username custom]
  (build-params :post "account/check-username" {:username username} custom))

(defn install-setup-register-check-email
  [email custom]
  (build-params :post "account/check-email" {:email email} custom))

(defn install-setup-verify-verify
  [verification-key with-login? custom]
  (build-params
   :post
   "account/verify"
   {:verification_key verification-key
    :with_login with-login?}
   custom))

(defn install-setup-verify-check
  [custom]
  (build-params
   :get
   "account/check-verify"
   {}
   custom))

(defn install-setup-document-fetch
  [document-id content-type custom]
  (build-params
   :get
   (str "document/" (name document-id) "?type=" (name content-type))
   {}
   custom))

(defn install-setup-document-sign
  [document-id revision-id custom]
  (build-params
   :post
   (str "document/" (name document-id) "/sign")
   {:revision_id revision-id}
   custom))
