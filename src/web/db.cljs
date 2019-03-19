(ns web.db
  (:require [web.home.db :as home.db]
            [web.install.db :as install.db]))

;; Context

(defn get-context
  [global-db]
  (get-in global-db [:web]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:web] updated-local-db))

;; Model

(defn core-init-install-verify
  [verification-key]
  ;; (.replaceState (.-history js/window) {} nil "/")
  {:core {:client :web :state :install}
   :install (install.db/initial-verify verification-key)})

(defn core-init-default []
  {:core {:client :web :state :home}
   :home {}})

(defn core-init-invalid-query-string []
  (.replaceState (.-history js/window) {} nil "/")
  (core-init-default))

(defn core-init []
  (let [query-string (-> js/window .-location .-search)
        url-search (new js/URLSearchParams query-string)
        verification-key (.get url-search "verification-key")]
    (cond
      (not (nil? verification-key)) (core-init-install-verify verification-key)
      (not (= "" query-string)) (core-init-invalid-query-string)
      :else (core-init-default))))

;; (def initial
;;   {:home home.db/initial})

(defn set-username
  [db username]
  (assoc-in db [:meta :username] username))

(defn remove-username
  [db]
  (assoc-in db [:meta :username] nil))

(defn redirect-install-email-verification
  [db]
  (-> db
      (assoc-in [:core :state] :install)
      (assoc-in [:install] web.install.db/initial)
      (assoc-in [:install :screen] :setup)
      (assoc-in [:install :setup :active-step] :verify)))

;; (defn post-init
;;   [db cookies]
;;   (if (= :abc cookies)
;;     ))
