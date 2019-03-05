(ns game.server.software.requests
  (:require [driver.rest.request :as request]))

;; Download

(defn on-download-ok
  [db [status response] [{callback :on-ok}]]
  (println "download OK")
  (println status)
  (println response)
  {:db db})

(defn on-download-fail
  [db [status response] [{callback :on-fail}]]
  (println "download FAIL")
  (println status)
  (println response)
  {:db db})

(defn file-download
  [target-nip file-id cb]
  (request/software-file-download
   target-nip file-id
   {:on-ok [:game|server|software|req-download-ok on-download-ok cb]
    :on-fail [:game|server|software|req-download-fail on-download-fail cb]}))

;; Upload

(defn on-upload-ok
  [db [status response] [{callback :on-ok}]]
  (println "upload OK")
  (println status)
  (println response)
  {:db db})

(defn on-upload-fail
  [db [status response] [{callback :on-fail}]]
  (println "upload FAIL")
  (println status)
  (println response)
  {:db db})

(defn file-upload
  [target-nip file-id callback]
  (request/software-file-upload
   target-nip file-id
   {:on-ok [:game|server|software|req-upload-ok on-upload-ok callback]
    :on-fail [:game|server|software|req-upload-fail on-upload-fail callback]}))

;; Bruteforce

(defn on-bruteforce-ok
  [db [status response] [{callback :on-ok}]]
  (println "bruteforce OK")
  (println status)
  (println response)
  {:db db})

(defn on-bruteforce-fail
  [db [status response] [{callback :on-fail}]]
  (println "bruteforce FAIL")
  (println status)
  (println response)
  {:db db})

(defn bruteforce
  [gateway-id target-ip cb]
  (request/software-bruteforce
   gateway-id target-ip
   {:on-fail [:game|server|software|req-bruteforce-fail on-bruteforce-fail cb]
    :on-ok [:game|server|software|req-bruteforce-ok on-bruteforce-ok cb]}))

