(ns web.apps.software.cracker.db
  (:require [web.wm.db :as wm.db]
            [game.server.software.db :as software.db]))

(defn open-opts
  [file-id]
  {:file-id file-id
   :file-type :cracker
   :len-x 300
   :len-y 235
   :config {:icon-class "far fa-folder"
            :title "Cracker"}})

;; Model

(defn initial
  [ip]
  {:tab :bruteforce
   :bruteforce {:target-ip ip}})

;; Model > Bruteforce

(defn bruteforce-get-ip
  [db]
  (get-in db [:bruteforce :target-ip]))

;; Model > Overflow

;; Events API

(defn on-tab-click
  [db tab-id]
  (assoc db :tab tab-id))

;; WM API

(defn ^:export will-open
  [_ctx app-context args]
  [:open-app :software-cracker app-context args])
(defn ^:export did-open
  [{wm-db :wm game-db :game} app-context {arg-file-id :file-id arg-ip :ip}]
  (let [server-cid (wm.db/get-server-cid wm-db app-context)
        soft-db (software.db/get-context-game game-db server-cid)
        file-id (if (nil? arg-file-id)
                  (software.db/get-best-file-id soft-db :cracker :bruteforce)
                  arg-file-id)
        ip (if (nil? arg-ip)
             ""
             arg-ip)]
    [:ok (initial ip) (open-opts file-id)]))

;; Popup handlers

(defn ^:export popup-may-open
  [_ popup-type parent-id args]
  [:open-popup :software-cracker popup-type parent-id args])
(defn ^:export popup-may-close
  [_ctx popup-type family-ids _state args]
  [:close-popup :software-cracker popup-type family-ids args])
(defn ^:export popup-may-focus
  [_ctx popup-type family-ids _state args]
  [:focus-popup :software-cracker popup-type family-ids args])


