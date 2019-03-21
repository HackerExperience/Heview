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

(defn on-click-server-name
  [identifier _event]
  (he/dispatch [:web|hud|connection-info|toggle-server-selector identifier]))

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

(defn render-connection-entry-server
  [identifier session-info]
  (let [session-id (:session-id session-info)
        icon-text (if (= identifier :gateway)
                    (:gateway-name session-info)
                    (:endpoint-ip session-info))
        is-active? (= identifier (:active-context session-info))]
    [:div.hud-ci-server
     (when is-active?
       {:class :hud-ci-server-active-desktop})
     [:div.hud-ci-server-icon
      [:i.fas.fa-server]]
     [:div.hud-ci-server-name
      {:on-click #(on-click-server-name identifier %)}
      [:span icon-text
       [:i.fa.fa-caret-down.hud-ci-server-selector]]]]))

(defn on-notification-panel-close
  []
  (he/dispatch [:web|hud|connection-info|close-notification-panel]))

(defn on-notification-panel-unread
  [server-cid]
  (he/dispatch [:game|notifications|mark-all-read [:server server-cid]]))

(defn render-notification-panel
  [session-info]
  (let [panel-info (he/subscribe [:web|hud|connection-info|notification-panel])]
    (when-not (nil? panel-info)
      (let [[server-cid server-name]
            (if (= panel-info :gateway)
              [(:gateway-cid session-info) (:gateway-name session-info)]
              [(:endpoint-cid session-info) (:endpoint-ip session-info)])
            entries (he/subscribe [:game|server|notification|all server-cid])
            header-name (str "notifications.*@" server-name)]
        [ui.components/notification-panel
         {:entries entries
          :header-str header-name
          :class-prefix :hud-ci
          :position-class (str "hud-ci-np-" (name panel-info))
          :on-click #(println "Clickouuuuu " %)
          :on-close on-notification-panel-close
          :on-unread #(on-notification-panel-unread server-cid)}]))))

(defn on-server-selector-close
  [_comp]
  (he/dispatch [:web|hud|connection-info|close-server-selector]))

(defn on-server-selector-changed-gateway
  [server-id]
  (println "Mudouuu " server-id)
  (he/dispatch [:web|wm|set-active-session server-id]))

(defn render-server-selector-dd-gateway
  [_meta entry]
  (println "Rendering " entry)
  (let [server
        (he/subscribe
         [:web|hud|connection-info|server-selector|entry|gateway (:id entry)])
        class (if (= (:group entry) "Multiplayer")
                :hud-ci-server-selector-entry-mp
                :hud-ci-server-selector-entry-sp)]
    [:div.hud-ci-server-selector-entry
     {:class class}
     [:div.hud-ci-server-selector-entry-icon
      [:i.fas.fa-server]]
     [:div.hud-ci-server-selector-entry-body
      [:div.hud-ci-server-selector-entry-body-name
       [:span (:name server)]]
      [:div.hud-ci-server-selector-entry-body-hardware
       [:span (:hardware-cpu server)]
       [:span.hud-ci-server-selector-entry-body-hardware-dot]
       [:span (:hardware-ram server)]
       [:span.hud-ci-server-selector-entry-body-hardware-dot]
       [:span (:hardware-disk server)]
       [:span.hud-ci-server-selector-entry-body-hardware-dot]
       [:span (:hardware-net server)]]]]))

(defn render-server-selector-gateway
  [session-info]
  (let [entries
        (he/sub [:web|hud|connection-info|server-selector|entries :gateway])]
    [:div.hud-ci-server-selector-gateway
     [ui.components/dropdown
      {:entries entries
       :entry-id (:gateway-cid session-info)
       :on-change on-server-selector-changed-gateway
       :grouped? true
       :search? false
       :drop-showing? true
       :class-prefix "hud-ci-server-selector"
       :callbacks {:unmount on-server-selector-close}
       :renderers {:drop-simple render-server-selector-dd-gateway}}]]))

(defn render-server-selector-endpoint
  [_]
  [:div "WUTDASFASDF"])

(defn render-server-selector
  [session-info]
  (let [selector-info (he/subscribe [:web|hud|connection-info|server-selector])]
    (when-not (nil? selector-info)
      (if (= :gateway selector-info)
        [render-server-selector-gateway session-info]
        [render-server-selector-endpoint session-info]))))

(defn display-connection-info []
  (let [session-info (he/subscribe [:web|hud|connection-info|session-info])]
    [:div.hud-ci-area
     [:div.hud-ci-gateway-area
      [render-connection-entry-side :gateway session-info]
      [render-connection-entry-server :gateway session-info]]
     [:div.hud-ci-bounce-area]
     (if-not (nil? (:endpoint-cid session-info))
       [:div.hud-ci-endpoint-area
        [render-connection-entry-server :endpoint session-info]
        [render-connection-entry-side :endpoint session-info]]
       [:div.hud-ci-endpoint-area
        [:div.hud-ci-endpoint-nil
         [:i.fas.fa-ban]
         [:span "Not connected"]]])
     [render-notification-panel session-info]
     [render-server-selector session-info]]))

(defn view []
  [:div#hud-connection-info
   [display-connection-info]])
