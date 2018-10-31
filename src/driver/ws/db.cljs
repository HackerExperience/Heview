(ns driver.ws.db
  (:require ["phoenix" :as phoenix]
            [he.core :as he]))


(defn create-socket
  [token]
  (new phoenix/Socket
                    "wss://localhost:4000"
                    (clj->js {:params {:token token}})))

(defn connect
  [db token]
  (let [socket (create-socket token)]
    (.connect socket)
    (assoc-in db [:driver :ws :socket] socket)))

(defn get-socket-from-db
  [db]
  (get-in db [:driver :ws :socket]))

(defn teardown
  [db]
  (let [socket (get-socket-from-db db)]
        (.disconnect socket))
    (assoc-in db [:driver :ws :socket] nil))

;; Account ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn account-channel
  [socket account-id]
  (.channel socket (str "account:" account-id) (clj->js {})))

(defn account-join-ok
  [resp]
  (let [data (js->clj (.-data resp) :keywordize-keys true)]
    (he/dispatch [:game|bootstrap-account data])))

(defn account-join-fail
  [resp]
  (he/dispatch [:game|bootstrap-account-fail :server-error]))

(defn account-join-timeout
  [resp]
  (he/dispatch [:game|bootstrap-account-fail :timeout]))

(defn join-account
  [db account-id]
  (let [socket (get-socket-from-db db)
        joined-channel (.join (account-channel socket account-id))]
    (.receive joined-channel "ok" account-join-ok)
    (.receive joined-channel "error" account-join-fail)
    (.receive joined-channel "timeout" account-join-timeout)))

;; Server ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn server-channel
  [socket server-id]
  (.channel socket (str "server:" server-id) (clj->js {})))

(defn server-join-ok
  [server-id resp]
  (let [data (js->clj (.-data resp) :keywordize-keys true)]
    (he/dispatch [:game|bootstrap-server server-id data])))

(defn server-join-fail
  [resp]
  (println "srv fail")
  (println resp))

(defn server-join-timeout
  [resp]
  (println "srv timeout")
  (println resp))

(defn join-server
  [db server-id]
  (let [socket (get-socket-from-db db)
        joined-channel (.join (server-channel socket server-id))]
    (.receive joined-channel "ok" (partial server-join-ok server-id))
    (.receive joined-channel "error" server-join-fail)
    (.receive joined-channel "timeout" server-join-timeout)))
