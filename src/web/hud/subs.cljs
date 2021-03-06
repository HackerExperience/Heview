(ns web.hud.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]
            [web.hud.conky.subs]
            [web.hud.db :as hud.db]))

(rf/reg-sub
 :web|hud
 :<- [:web]
 (fn [db _]
   (:hud db)))

;; Launcher

(rf/reg-sub
 :web|hud|launcher
 :<- [:web|hud]
 (fn [db _]
   (:launcher db)))

(rf/reg-sub
 :web|hud|launcher|show-overlay?
 :<- [:web|hud|launcher]
 (fn [db _]
   (:show-overlay? db)))

(rf/reg-sub
 :web|hud|launcher|config
 :<- [:web|hud|launcher]
 (fn [db _]
   (:config db)))

;; Taskbar

(defn- calculate-taskbar-display-type
  [{viewport-x :x} entries]
  (let [taskbar-width (- viewport-x 400)
        max-entries (int (/ taskbar-width 200))]
    (if (> (count entries) max-entries)
      :small
      :full)))

(defn- taskbar-entries-reducer
  [acc app-id]
  (let [{icon-class :icon-class
         timestamp :open-timestamp
         show-taskbar :show-taskbar
         name :title} (he/subscribe [:web|wm|window|config app-id])]
    (if show-taskbar
      (assoc acc timestamp {:app-id app-id
                            :name name
                            :icon-class icon-class})
      acc)))

(rf/reg-sub
 :web|hud|taskbar|entries
 (fn [_ _]
   [(he/subscribed [:web|wm|current-session|apps])])
 (fn [[apps]]
   (let [entries (reduce taskbar-entries-reducer {} apps)
         viewport (he/subscribe [:web|wm|viewport])
         display-type (calculate-taskbar-display-type viewport entries)]
     {:entries (into
                (sorted-map-by <)
                entries)
      :display-type display-type})))

;; Connection Info

(rf/reg-sub
 :web|hud|connection-info
 :<- [:web|hud]
 (fn [db]
   (:connection-info db)))

(rf/reg-sub
 :web|hud|connection-info|notification-panel
 :<- [:web|hud|connection-info]
 (fn [db]
   (:notification-panel db)))

(rf/reg-sub
 :web|hud|connection-info|session-info
 (fn [_]
   [(he/subscribed [:web|wm|active-session])])
 (fn [[session-id]]
   (let [{gateway-cid :gateway
          endpoint-cid :endpoint} (he/subscribe [:web|wm|session session-id])
         gtw-name (he/subscribe [:game|server|data|meta|hostname gateway-cid])
         endp-link (when-not (nil? endpoint-cid)
                     (he/subscribe [:game|server|endpoint|link endpoint-cid]))
         active-context (if (= session-id gateway-cid)
                          :gateway
                          :endpoint)]
     {:gateway-cid gateway-cid
      :gateway-name gtw-name
      :endpoint-cid endpoint-cid
      :endpoint-ip (:ip endp-link)
      :session-id session-id
      :active-context active-context})))

(rf/reg-sub
 :web|hud|connection-info|server-selector
 :<- [:web|hud|connection-info]
 (fn [db]
   (:server-selector db)))

(rf/reg-sub
 :web|hud|connection-info|server-selector|entries
 (fn [[_ server-type]]
   (if (= server-type :gateway)
     [(he/subscribed [:game|server|meta|gateways|single-player])
      (he/subscribed [:game|server|meta|gateways|multi-player])]
     [(he/subscribed [:game|server|meta|endpoints])]))
 (fn [servers [_ server-type]]
   (if (= server-type :gateway)
     (let [[servers-sp servers-mp] servers]
       (hud.db/ci-server-selector-format-entries-gateway servers-sp servers-mp))
     (hud.db/ci-server-selector-format-entries-endpoint (first servers)))))

(rf/reg-sub
 :web|hud|connection-info|server-selector|entry|gateway
 (fn [[_ server-id]]
   [(he/subscribed [:game|server|data|meta|hostname server-id])])
 (fn [[hostname]]
   {:name hostname
    ;; Hardware data is TODO
    :hardware-cpu "500Mhz"
    :hardware-ram "128MB"
    :hardware-disk "10GB"
    :hardware-net "1Mbit"}))
