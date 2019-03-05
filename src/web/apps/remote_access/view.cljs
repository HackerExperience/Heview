(ns web.apps.remote-access.view
  (:require [cljs.core.match :refer-macros [match]]
            [he.core :as he]))

;; Browse ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn on-browse-ip-submit
  [app-id _]
  (he/dispatch [:web|apps|remote-access|browse|submit app-id]))

(defn on-browse-ip-change
  [app-id event]
  (he/dispatch [:web|apps|remote-access|browse|ip|change
                app-id (-> event .-target .-value)]))

(defn view-browse-ip
  [app-id server-cid]
  (let [loading? (he/subscribe [:web|apps|remote-access|browse|loading? app-id])
        input-ip (he/subscribe [:web|apps|remote-access|ip app-id])]
    [:div.ra-browse
     [:div.ra-browse-input
      [:input.ui-input
       {:type :text
        :value input-ip
        :on-change #(on-browse-ip-change app-id %)}]]
     [:div.ra-browse-button
      (if-not loading?
        [:button.ui-btn
         {:on-click #(on-browse-ip-submit app-id %)}
         "Go"]
        [:div.ra-browse-button-spinner.ui-spinner-area
         [:i.fa.fa-spinner.fa-spin]])]]))

;; Auth ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn on-auth-pass-change
  [app-id event]
  (he/dispatch [:web|apps|remote-access|auth|pass|change
                app-id (-> event .-target .-value)]))

(defn on-auth-submit
  [app-id _]
  (he/dispatch [:web|apps|remote-access|auth|submit app-id]))

(defn on-auth-bruteforce
  [app-id ip _]
  (he/dispatch [:web|wm|app|open :software-cracker {:ip ip}]))

(defn view-authentication
  [app-id server-cid]
  (let [loading? (he/subscribe [:web|apps|remote-access|auth|loading? app-id])
        auth-pass (he/subscribe [:web|apps|remote-access|auth|pass app-id])
        ip (he/subscribe [:web|apps|remote-access|ip app-id])]
    [:div.ra-auth
     [:div.ra-auth-login-area
      [:div.ra-auth-login-username
       [:input.ui-input
        {:type :text :value "root" :readOnly true}]]
      [:div.ra-auth-login-password
       [:input.ui-input
        {:type :text
         :value auth-pass
         :placeholder "Password"
         :on-change #(on-auth-pass-change app-id %)}]]]
     [:div.ra-auth-action-area
      [:div.ra-auth-action-bruteforce-area
       [:button.ui-btn.btn-dual
        {:tip "Start a bruteforce attack to retrieve the server password."
         :on-click #(on-auth-bruteforce app-id ip %)}
        [:i.fas.fa-unlock]
        [:span "Bruteforce"]]]
      [:div.ra-auth-action-login-area
       [:button.ui-btn.btn-dual.btn-primary
        {:on-click #(on-auth-submit app-id %)
         :tip "Login to the remote server using the above credentials."}
        [:i.fas.fa-sign-in-alt]
        [:span "Login"]]]]]))

;; Remote ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn on-remote-app
  [app-id app-type event]
  (he/dispatch [:web|wm|app|open app-type [] :remote]))

(defn view-remote
  [app-id server-cid]
  [:div.ra-remote
   [:button
    {:on-click #(on-remote-app app-id :log-viewer %)}
    "Open Log viewer"]])

;; Entrypoint

(defn ^:export view
  [app-id server-cid]
  [:div.ra-container
   (let [screen (he/subscribe [:web|apps|remote-access|screen app-id])]
     (match screen
            :browse (view-browse-ip app-id server-cid)
            :auth (view-authentication app-id server-cid)
            :remote (view-remote app-id server-cid)
            other (he.error/runtime (str "Invalid screen" other))))])
