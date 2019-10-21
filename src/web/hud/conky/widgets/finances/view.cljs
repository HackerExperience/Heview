(ns web.hud.conky.widgets.finances.view
  (:require [he.core :as he]
            [web.hud.conky.ui :as ckui]))

(defn view []
  [:div.hud-ckw-finances
   [:div.hud-ckui-row
    [:span.hud-ckui-label.hud-ckui-fill-1 "Cash:"]
    [:span.hud-ckui-item.hud-ckui-pull-right "$0.00"]
    [:span.hud-ckui-label "Hexcoins:"]
    [:span.hud-ckui-item.hud-ckui-pull-right "0.00000000 HEX"]]

   [:div.hud-ckui-row
    [:span.hud-ckui-label.hud-ckui-fill-2 "HEX price:"]
    [:span.hud-ckui-item.hud-ckui-pull-right "$1239.38"]
    [:span.hud-ckui-label.hud-ckui-fill-1 "Chart:"]
    ;; Chart is todo
    [:div.hud-ckui-fill-2
     [ckui/progress-bar "100%"]]]])
