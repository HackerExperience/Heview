(ns game.server.requests
  (:require [driver.rest.request :as request]
            [game.server.db :as server.db]))

(defn on-login-ok
  [gdb [status response] [{callback :on-ok} gateway-id ip]]
  (let [new-gdb (as-> (server.db/get-context gdb) ldb
                  (server.db/bootstrap-server ldb response (str ip "@::"))
                  (server.db/set-context gdb ldb))]
     {:db new-gdb
      :dispatch-n (list
                   (request/callback-event callback status response)
                   [:web|wm|on-remote-login gateway-id (str ip "@::")])}))

(defn on-login-fail
  [_ [status response] [{callback :on-fail}]]
  (request/wrap-callback callback status response))

(defn login
  [server-cid ip password callback]
  (request/server-login
   server-cid ip password
   {:on-ok [:game|server|req-login-ok on-login-ok callback server-cid ip]
    :on-fail [:game|server|req-login-fail on-login-fail callback]}))
