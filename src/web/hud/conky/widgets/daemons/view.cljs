(ns web.hud.conky.widgets.daemons.view
  (:require [he.core :as he]))

(defn view []
  [:div.hud-ckw-dmn
   ;; [:div.hud-ckui-row
   ;;  [:span.hud-ckui-label "Total:"]
   ;;  [:span.hud-ckui-item "3"]]
   [:div.hud-ckui-row
    [:span.hud-ckui-fill-1]
    [:span.hud-ckui-fill-5 "Name"]
    [:span.hud-ckui-fill-5 "Resources"]
    [:span.hud-ckui-fill-1 "Time"]]

   [:div.hud-ckui-row.hud-ckui-row-hoverable.hud-ckui-item
    [:i.fa.fa-search.hud-ckui-fill-1]
    [:span.hud-ckui-fill-5 "Password Hasher"]
    [:span.hud-ckui-fill-5
     [:i.fas.fa-microchip.hud-ckw-prc-recur-resource-icon]
     [:span.hud-ckw-prc-recur-resource-text "30.0%"]
     [:i.fas.fa-microchip.hud-ckw-prc-recur-resource-icon]
     [:span.hud-ckw-prc-recur-resource-text "30.0%"]]
    [:span.hud-ckui-fill-1 "10d4h"]]

   [:div.hud-ckui-row.hud-ckui-row-hoverable.hud-ckui-item
    [:i.fas.fa-fire.hud-ckui-fill-1]
    [:span.hud-ckui-fill-5 "Firewall"]
    [:span.hud-ckui-fill-5
     [:i.fas.fa-microchip.hud-ckw-prc-recur-resource-icon]
     [:span.hud-ckw-prc-recur-resource-text "9.02%"]
     [:i.fas.fa-microchip.hud-ckw-prc-recur-resource-icon]
     [:span.hud-ckw-prc-recur-resource-text "0.01%"]]
    [:span.hud-ckui-fill-1 "10d4h"]]

   [:div.hud-ckui-row.hud-ckui-row-hoverable.hud-ckui-item
    [:i.fas.fa-skull.hud-ckui-fill-1]
    [:span.hud-ckui-fill-5 "IDPS"]
    [:span.hud-ckui-fill-5
     [:i.fas.fa-microchip.hud-ckw-prc-recur-resource-icon]
     [:span.hud-ckw-prc-recur-resource-text "2.29%"]
     [:i.fas.fa-microchip.hud-ckw-prc-recur-resource-icon]
     [:span.hud-ckw-prc-recur-resource-text "1.17%"]]
    [:span.hud-ckui-fill-1 "10d4h"]]])
