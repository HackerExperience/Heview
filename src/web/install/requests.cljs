(ns web.install.requests
  (:require [driver.rest.request :as request]))

;; Register

(defn setup-register-submit
  [args callbacks]
  (request/install-setup-register-submit
   args
   {:on-ok [:web|install|setup|step|register|req-submit-ok callbacks]
    :on-fail [:web|install|setup|step|register|req-submit-fail callbacks]}))

(defn setup-register-check-username
  [username callbacks]
  (request/install-setup-register-check-username
   username
   {:on-ok [:web|install|setup|step|register|check-username-ok callbacks]
    :on-fail [:web|install|setup|step|register|check-username-fail callbacks]}))

(defn setup-register-check-email
  [email callbacks]
  (request/install-setup-register-check-email
   email
   {:on-ok [:web|install|setup|step|register|check-email-ok callbacks]
    :on-fail [:web|install|setup|step|register|check-email-fail callbacks]}))

;; Verify

(defn setup-verify-verify
  [verification-key callbacks]
  (request/install-setup-verify-verify
   verification-key false
   {:on-ok [:web|install|setup|step|verify|verify-ok callbacks]
    :on-fail [:web|install|setup|step|verify|verify-fail callbacks]}))

(defn setup-verify-verify-login
  [verification-key callbacks]
  (request/install-setup-verify-verify
   verification-key true
   {:on-ok [:web|install|setup|step|verify|verify-login-ok callbacks]
    :on-fail [:web|install|setup|step|verify|verify-login-fail callbacks]}))

(defn setup-verify-check
  [callbacks]
  (request/install-setup-verify-check
   {:on-ok [:web|install|setup|step|verify|check-ok callbacks]
    :on-fail [:web|install|setup|step|verify|check-fail callbacks]}))

;; Document

(defn setup-document-fetch
  [document-id content-type]
  (request/install-setup-document-fetch
   document-id content-type
   {:on-ok [:web|install|setup|document|fetch-ok document-id]
    :on-fail [:web|install|setup|document|fetch-fail document-id]}))

(defn setup-document-sign
  [document-id revision-id csrf-token]
  (request/install-setup-document-sign
   document-id revision-id
   {:on-ok [:web|install|setup|document|sign-ok document-id]
    :on-fail [:web|install|setup|document|sign-fail document-id]
    :csrf-token csrf-token}))
