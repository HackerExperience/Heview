(ns web.hud.conky.widgets.network.view
  (:require [he.core :as he]
            [web.hud.conky.ui :as ckui]))

(defn view []
  [:div.hud-ckw-net
   [ckui/widget-header-line "100.200.300.400@eth0"]
   [:div.hud-ckui-row
    [:span.hud-ckui-label.hud-ckui-fill-3 "Download:"]
    [:span.hud-ckui-item.hud-ckui-fill-2 "57 Kb/s"]
    [:span.hud-ckui-label.hud-ckui-fill-3 "Upload:"]
    [:span.hud-ckui-item "5 Mb/s"]]
   [:div.hud-ckui-row
    [:span.hud-ckui-label.hud-ckui-fill-5 "Link speed:"]
    [:span.hud-ckui-item.hud-ckui-fill-3 "10 Mbit"]
    [:span.hud-ckui-label.hud-ckui-fill-4 "Uptime:"]
    [:span.hud-ckui-item "20d4h30m"]]

   [ckui/widget-header-line "192.168.0.100@eth1"]
   [:div.hud-ckui-row
    [:span.hud-ckui-label.hud-ckui-fill-3 "Download:"]
    [:span.hud-ckui-item.hud-ckui-fill-2 "0 Kb/s"]
    [:span.hud-ckui-label.hud-ckui-fill-3 "Upload:"]
    [:span.hud-ckui-item "0 Kb/s"]]
   [:div.hud-ckui-row
    [:span.hud-ckui-label.hud-ckui-fill-5 "Link speed:"]
    [:span.hud-ckui-item.hud-ckui-fill-3 "1 Gbit"]
    [:span.hud-ckui-label.hud-ckui-fill-4 "Uptime:"]
    [:span.hud-ckui-item "20d4h30m"]]
   ])
