(ns web.apps.remote-access.db
  (:require [cljs.core.match :refer-macros [match]]
            [web.wm.db :as wm.db]
            [web.apps.db :as apps.db]
            [web.apps.remote-access.validators :as v]))

(defn open-opts
  [args]
  (let [screen (get args :screen :browse)
        [len-x len-y title] (cond
                              (= :browse screen) [175 75 "Browse"]
                              (= :auth screen) [200 160 "Login"])]
    {:len-x len-x
     :len-y len-y
     :config {:title title
              :contextable false
              :show-context false
              :icon-class "fas fa-network-wired"}}))

(defn initial
  [server-cid args]
  (let [screen (get args :screen :browse)
        ip (get args :ip "")
        auth-pass (get args :auth-pass "")]
    {:server-cid server-cid
     :screen screen
     :ip ip
     :data-browse {:valid? false
                   :show-validation? false
                   :loading? false}
     :data-auth {:valid? false
                 :show-validation? false
                 :user "root"
                 :pass auth-pass
                 :loading? false}
     :data-remote {}}))

;; Model

(defn get-server-cid
  [state]
  (:server-cid state))
  ;(v/v v/server-cid (:server-cid state)))

(defn get-ip
  [state]
  (v/v v/ip (:ip state)))

(defn update-ip
  [state new-ip]
  (assoc state :ip new-ip))

;; Transitions

(defn transition-browse-auth
  [state]
  (assoc state :screen :auth))

(defn transition-auth-remote
  [state]
  (assoc state :screen :remote))

;; Model > Browse

(defn browse-start-loading
  [state]
  (assoc-in state [:data-browse :loading?] true))

(defn browse-stop-loading
  [state]
  (assoc-in state [:data-browse :loading?] false))

(defn auth-start-loading
  [state]
  (assoc-in state [:data-auth :loading?] true))

(defn auth-stop-loading
  [state]
  (assoc-in state [:data-auth :loading?] false))

;; Callbacks > Browse

(defn browse-submit-callback
  [app-id ip]
  {:on-ok [:web|apps|remote-access|browse|submit|ok app-id ip]
   :on-fail [:web|apps|remote-access|browse|submit|fail app-id ip]})

(defn browse-submit-ok
  [state]
  (-> state
      (browse-stop-loading)
      (transition-browse-auth)))

(defn browse-submit-fail
  [state]
  (browse-stop-loading state))

(defn browse-submit-fail-config
  [status app-id ip]
  (let [text-fn #(cond (= % 404) (str "The IP address " ip " was not found.")
                       (= % 600) (str "The IP address '" ip "' is invalid."))]
    (he.error/generic-response-config status text-fn app-id)))

;; Model > Auth

(defn auth-get-pass
  [state]
  (get-in state [:data-auth :pass]))

(defn auth-update-pass
  [state new-pass]
  (assoc-in state [:data-auth :pass] new-pass))

;; Callbacks > Auth

(defn auth-submit-callback
  [app-id pass]
  {:on-ok [:web|apps|remote-access|auth|submit|ok app-id pass]
   :on-fail [:web|apps|remote-access|auth|submit|fail app-id pass]})

(defn auth-submit-ok
  [state]
  (-> state
      (auth-stop-loading)
      (transition-auth-remote)))

(defn auth-submit-fail
  [state]
  (auth-stop-loading state))

(defn auth-submit-fail-config
  [status app-id pass]
  (let [text-fn #(cond (= % 403) (str "The password " pass " is wrong."))]
    (he.error/generic-response-config status text-fn app-id)))

;; Public Interface

;; Events API

(defn browse-ip-change
  [state new-ip]
  (update-ip state new-ip))

(defn browse-submit
  [state]
  (browse-start-loading state))

(defn auth-pass-change
  [state new-pass]
  (auth-update-pass state new-pass))

(defn auth-submit
  [state]
  (auth-start-loading state))

;; WM API

(defn ^:export did-open
  [{wm-db :wm} _ args]
  (println ":args are " args)
  (let [server-cid (wm.db/get-active-session wm-db)]
    [:ok (initial server-cid args) (open-opts args)]))

(defn ^:export will-focus
  [{app-db :app} app-id _state _args]
  (let [query-app (apps.db/query app-db app-id)
        blocking-popup-id (apps.db/query-has-blocking-popups query-app)]
    (if-not blocking-popup-id
      [:focus app-id]
      [:vibrate-focus blocking-popup-id {:subfocus app-id}])))
