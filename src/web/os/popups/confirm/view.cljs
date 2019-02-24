(ns web.os.popups.confirm.view
  (:require [he.core :as he]))

(defn on-click
  [app-id btn-config _event]
  (he/dispatch [:web|os|popups|confirm|btn-click app-id (:event btn-config)]))

(defn render-button
  [app-id btn-config]
  [:div.os-cfm-button
   [:button.ui-btn
    {:on-click #(on-click app-id btn-config %)
     :class (:class btn-config)}
    (:text btn-config)]])

(defn render-buttons
  [app-id config]
  [:div.os-cfm-button-area
   (let [btns (get config :btns [:btn-1 :btn-2])]
     (for [btn-id btns]
       ^{:key btn-id} [render-button app-id (get config btn-id)]))])

(defn view-body
  [app-id config]
  [:div.os-cfm-body
   [:div.os-cfm-message-area
    [:div.os-cfm-message-icon-area
     [:i.fa.fa-exclamation-triangle]]
    [:div.os-cfm-message-text-area
     (:text config)]]])

(defn view-footer
  [app-id config]
  [:div.os-cfm-footer
   [render-buttons app-id config]])

(defn ^:export view
  [app-id server-cid]
  (let [config (he/subscribe [:web|os|popups|confirm|config app-id])]
    [:div.os-cfm-container
     [view-body app-id config]
     [view-footer app-id config]]))
