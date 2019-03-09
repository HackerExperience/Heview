(ns web.hud.connection-info.view
  (:require [he.core :as he]))

(defn on-switch-session
  [identifier session-info _event]
  (when-not (= identifier (:active-context session-info))
    (let [server-cid (if (= identifier :gateway)
                       (:gateway-cid session-info)
                       (:endpoint-cid session-info))]
      (he/dispatch [:web|wm|set-active-session server-cid]))))

(defn render-connection-entry-side
  [identifier session-info]
  (let [session-id (:session-id session-info)
        is-active? (= identifier (:active-context session-info))]
    [:div.hud-ci-side
     [:div.hud-ci-side-desktop
      {:on-click #(on-switch-session identifier session-info %)
       :class (when is-active?
                [:hud-ci-side-active-desktop])}
      [:i.fas.fa-desktop]]
     [:div.hud-ci-side-bell
      [:i.far.fa-bell]]]))

(defn render-connection-entry-icon
  [identifier session-info]
  (let [session-id (:session-id session-info)
        icon-text (if (= identifier :gateway)
                    (:gateway-name session-info)
                    (:endpoint-ip session-info))
        is-active? (= identifier (:active-context session-info))]
    [:div.hud-ci-icon
     (when is-active?
       {:class :hud-ci-icon-active-desktop})
     [:i.fas.fa-server]
     [:span icon-text]]))

(defn display-connection-info []
  (let [session-info (he/subscribe [:web|hud|connection-info])]
    [:div.hud-ci-area
     [:div.hud-ci-gateway-area
      {:on-click #(he/dispatch [:web|wm|app|open :log-viewer])}
      [render-connection-entry-side :gateway session-info]
      [render-connection-entry-icon :gateway session-info]]
     [:div.hud-ci-bounce-area]
     (if-not (nil? (:endpoint-cid session-info))
       [:div.hud-ci-endpoint-area
        [render-connection-entry-icon :endpoint session-info]
        [render-connection-entry-side :endpoint session-info]]
       [:div.hud-ci-endpoint-area
        [:div.hud-ci-endpoint-nil
         [:i.fas.fa-ban]
         [:span "Not connected"]]])]))

(defn view []
  [:div#hud-connection-info
   [display-connection-info]])
