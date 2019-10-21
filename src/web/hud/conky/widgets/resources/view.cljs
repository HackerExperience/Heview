(ns web.hud.conky.widgets.resources.view
  (:require [he.core :as he]
            [web.hud.conky.ui :as ckui]))

(defn- random-key []
  (int (* 1000 (.random js/Math))))

(defn view-resource
  [resource]
  [:div.hud-ckui-row.hud-ckui-row-hoverable
   [:i.fas.fa-microchip.hud-ckui-item.hud-ckui-fill-1]
   [:span.hud-ckui-item.hud-ckui-fill-3 (:total resource)]
   [:span.hud-ckui-item.hud-ckui-fill-4 (:used-units resource)
    [:span.hud-ckui-subitem (str " (" (:used-pct resource) "%)")]]
   [:span.hud-ckui-item.hud-ckui-fill-3 (:avail resource)]])

(defn view []
  (let [resources (he/subscribe [:web|hud|conky|widget|resources|resources])]
    [:div.hud-ckw-resources
     [:div.hud-ckui-row
      [:span.hud-ckui-fill-1]
      [:span.hud-ckui-fill-3 "Total"]
      [:span.hud-ckui-fill-4 "Allocated"]
      [:span.hud-ckui-fill-3 "Available"]]
     (for [resource resources]
       ^{:key (str "ckw-res-" (random-key))} [view-resource resource])]))
