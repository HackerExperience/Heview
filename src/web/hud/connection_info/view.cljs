(ns web.hud.connection-info.view
  (:require [he.core :as he]
            [web.ui.components :as ui.components]))

(defn on-switch-session
  [identifier session-info _event]
  (when-not (= identifier (:active-context session-info))
    (let [server-cid (if (= identifier :gateway)
                       (:gateway-cid session-info)
                       (:endpoint-cid session-info))]
      (he/dispatch [:web|wm|set-active-session server-cid]))))

(defn on-click-bell
  [identifier session-info _event]
  (he/dispatch [:web|hud|connection-info|toggle-notification-panel identifier]))

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
     [:div.hud-ci-side-bell.ui-c-np-open-area
      {:on-click #(on-click-bell identifier session-info %)}
      [:i.far.fa-bell.ui-c-np-open-area]]]))

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

(defn on-notification-panel-close
  []
  (he/dispatch [:web|hud|connection-info|close-notification-panel]))

(defn on-notification-panel-unread
  [server-cid]
  (he/dispatch [:game|notifications|mark-all-read [:server server-cid]]))

(defn render-notification-panel
  [notif-panel session-info]
  (when-not (nil? notif-panel)
    (let [[server-cid server-name]
          (if (= notif-panel :gateway)
            [(:gateway-cid session-info) (:gateway-name session-info)]
            [(:endpoint-cid session-info) (:endpoint-ip session-info)])
          entries (he/subscribe [:game|server|notification|all server-cid])
          header-name (str "notifications.*@" server-name)]
      [ui.components/notification-panel
       {:entries entries
        :header-str header-name
        :class-prefix :hud-ci
        :position-class (str "hud-ci-np-" (name notif-panel))
        :on-click #(println "Clickouuuuu " %)
        :on-close on-notification-panel-close
        :on-unread #(on-notification-panel-unread server-cid)}])))

(defn display-connection-info []
  (let [notif-panel (he/subscribe [:web|hud|connection-info|notification-panel])
        session-info (he/subscribe [:web|hud|connection-info|session-info])]
    [:div.hud-ci-area
     [:div.hud-ci-gateway-area
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
         [:span "Not connected"]]])
     [render-notification-panel notif-panel session-info]]))

(defn view []
  [:div#hud-connection-info
   [display-connection-info]])
