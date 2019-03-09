(ns web.os.popups.confirm.view
  (:require [he.core :as he]))

(defn on-click
  [app-id btn-config _event]
  (he/dispatch [:web|os|popups|confirm|btn-click app-id (:event btn-config)]))

(defn view-body
  [app-id config]
  [:div.a-os-cfm-body
   [:div.a-os-cfm-b-message-area
    [:div.a-os-cfm-b-message-icon-area
     [:i.fa.fa-exclamation-triangle]]
    [:div.a-os-cfm-b-message-text-area
     (:text config)]]])

(defn render-button
  [app-id btn-config]
  [:div.a-os-cfm-f-button
   [:button.ui-btn
    {:on-click #(on-click app-id btn-config %)
     :class (:class btn-config)}
    (:text btn-config)]])

(defn render-buttons
  [app-id config]
  [:div.a-os-cfm-f-button-area
   (let [btns (get config :btns [:btn-1 :btn-2])]
     (for [btn-id btns]
       ^{:key btn-id} [render-button app-id (get config btn-id)]))])

(defn view-footer
  [app-id config]
  [:div.a-os-cfm-footer
   [render-buttons app-id config]])

(defn ^:export view
  [app-id server-cid]
  (let [config (he/subscribe [:web|os|popups|confirm|config app-id])]
    [:div.a-os-cfm-container
     [view-body app-id config]
     [view-footer app-id config]]))
