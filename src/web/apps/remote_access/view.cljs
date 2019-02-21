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
    [:div
     [:div
      [:input
       {:type :text
        :value input-ip
        :on-change #(on-browse-ip-change app-id %)}]
      [:button
       {:on-click #(on-browse-ip-submit app-id %)}
       "Go"]]
     [:span (if loading?
              "LOADLINGLAODIANSDFIASD"
              "Idle")]]))

;; Auth ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn on-auth-pass-change
  [app-id event]
  (he/dispatch [:web|apps|remote-access|auth|pass|change
                app-id (-> event .-target .-value)]))

(defn on-auth-submit
  [app-id _]
  (he/dispatch [:web|apps|remote-access|auth|submit app-id]))

(defn view-authentication
  [app-id server-cid]
  (let [loading? (he/subscribe [:web|apps|remote-access|auth|loading? app-id])
        auth-pass (he/subscribe [:web|apps|remote-access|auth|pass app-id])]
    [:div
     [:input
      {:type :text
       :value "root"
       :readOnly true}]
     [:input
      {:type :text
       :value auth-pass
       :on-change #(on-auth-pass-change app-id %)}]
     [:button
      {:on-click #(on-auth-submit app-id %)}
      "Login"]
     [:span (if loading?
              "LAODINGAEOFKDSFODFK"
              "Idle")]]))

;; Remote ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn on-remote-app
  [app-id app-type event]
  (he/dispatch [:web|wm|app|open app-type :remote]))

(defn view-remote
  [app-id server-cid]
  [:div
   [:button
    {:on-click #(on-remote-app app-id :log-viewer %)}
    "Open Log viewer"]])

;; Entrypoint

(defn ^:export view
  [app-id server-cid]
  (let [screen (he/subscribe [:web|apps|remote-access|screen app-id])]
    (match screen
           :browse (view-browse-ip app-id server-cid)
           :auth (view-authentication app-id server-cid)
           :remote (view-remote app-id server-cid)
           other (he.error/runtime (str "Invalid screen" other)))))
