(ns game.server.log.requests
  (:require [driver.rest.request :as request]))

(defn on-edit-ok
  [db [status response] [{callback :on-ok}]]
  (println "OK")
  (println status)
  (println response)

  {:db db})

(defn on-edit-fail
  [db [status response] [{callback :on-fail}]]
  (println "FAIL")
  (println status)
  (println response)

  {:db db})

(defn forge-edit
  [server-cid log callback]
  (request/log-forge-edit
   server-cid log
   {:on-ok [:game|server|log|req-edit-ok on-edit-ok callback]
    :on-fail [:game|server|log|req-edit-fail on-edit-fail callback]}))
