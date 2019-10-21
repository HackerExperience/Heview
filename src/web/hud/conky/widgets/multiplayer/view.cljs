(ns web.hud.conky.widgets.multiplayer.view
  (:require [he.core :as he]
            [web.hud.conky.ui :as ckui]))

(defn view []
  [:div.hud-ckw-multiplayer
   [:div.hud-ckui-row
    [:span.hud-ckui-label.hud-ckui-fill-1 "Global:"]
    [:span.hud-ckui-item.hud-ckui-fill-3.hud-ckui-pull-right "#3,209"]
    [:span.hud-ckui-label.hud-ckui-fill-1 "Country:"]
    [:span.hud-ckui-item.hud-ckui-fill-2.hud-ckui-pull-right "#193"]
    [:span.hud-ckui-label.hud-ckui-fill-1 "City:"]
    [:span.hud-ckui-item.hud-ckui-fill-1.hud-ckui-pull-right "#2"]]

   [:div.hud-ckui-row
    [:span.hud-ckui-label.hud-ckui-fill-1 "Power:"]
    [:span.hud-ckui-item.hud-ckui-fill-2.hud-ckui-pull-right "0"]
    [:span.hud-ckui-label.hud-ckui-fill-1 "Level:"]
    [:span.hud-ckui-item.hud-ckui-fill-3.hud-ckui-pull-right "Noob (1)"]]

   [:div.hud-ckui-row
    [:span.hud-ckui-label.hud-ckui-fill-1 "Progress:"]
    [:div.hud-ckui-fill-3
     [ckui/progress-bar "40%"]]]])
