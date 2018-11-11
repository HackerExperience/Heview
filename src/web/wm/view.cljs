(ns web.wm.view
  (:require [he.core :as he]
            [web.apps.dispatcher :as apps.dispatcher]))

(defn header-context-switch
  [app-id]
  (let [context (he/subscribe [:web|wm|apps|context app-id])]
    [:button
     {:on-click #(he/dispatch [:web|wm|app|switch-context app-id])}
     (str context " (switch)")]))

(defn render-app-header
  [app-id window-data]
  [:div.header "HEaderrrr"
   [header-context-switch app-id]
   [:button
    {:on-click #(he/dispatch [:web|wm|app|close app-id])}
    "Close"]])

(defn render-app-body
  [app-id]
  [:div.body
    [apps.dispatcher/dispatch-view :log-viewer app-id]])

(defn render-app
  [app-id]
  (let [window-data (he/subscribe [:web|wm|apps|window-data app-id])]
    [:div.app
     {:id (str "app-" app-id)}
     [render-app-header app-id window-data]
     [render-app-body app-id]]))

(defn render-session
  [sid]
  (let [apps (he/subscribe [:web|wm|session|apps sid])]
    [:div#session
      (for [app-id apps]
        ^{:key app-id} [render-app app-id])]))

(defn view
  []
  (let [session-id (he/subscribe [:web|wm|active-session])]
    [:div#wm
     [render-session session-id]]))
