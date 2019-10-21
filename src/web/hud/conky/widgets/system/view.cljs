(ns web.hud.conky.widgets.system.view
  (:require [reagent.core :as r]
            [he.core :as he]))

(defn set-uptime
  [state el-uptime]
  (set! (.-innerHTML el-uptime) (he.date/format-uptime (:diff @state))))

(defn create-uptime-interval
  [state el-uptime]
  (set-uptime state el-uptime)
  (let [ref (js/setInterval
             #(do (swap! state assoc :diff (+ 60 (:diff @state)))
                  (set-uptime state el-uptime))
             60000)]
    (swap! state assoc :interval-ref ref)))

(defn render-row-uptime-inner []
  (let [state (r/atom {:diff nil
                       :interval-ref nil})]
    (r/create-class
     {:reagent-render
      (fn []
        [:div.hud-ckui-row
         [:span.hud-ckui-label.hud-ckui-fill-1 "Uptime:"]
         [:span.hud-ckw-sys-uptime.hud-ckui-item.hud-ckui-pull-right]])
      :component-did-mount
      (fn [comp]
        (let [root (r/dom-node comp)
              el-uptime (.querySelector root ".hud-ckw-sys-uptime")
              uptime-diff (:uptime-diff (r/props comp))
              wait (he.date/milliseconds-till-next-minute)
              diff-after-wait (+ uptime-diff (int (/ wait 1000)))]
          (swap! state assoc :diff uptime-diff)
          (set-uptime state el-uptime)
          (js/setTimeout
           #(do (swap! state assoc :diff diff-after-wait)
                (create-uptime-interval state el-uptime))
           wait)))
      :component-will-unmount
      (fn [_]
        (let [ref (:interval-ref @state)]
          (when-not (nil? ref)
            (js/clearInterval ref))))})))

(defn render-row-uptime []
  (let [uptime-diff (he/subscribe [:web|hud|conky|widget|system|uptime-diff])]
    (fn []
      [render-row-uptime-inner {:uptime-diff uptime-diff}])
    ;; [:div.hud-ckui-row
    ;;  [:span.hud-ckui-label.hud-ckui-fill-1 "Uptime:"]
    ;;  [:span.hud-ckui-item.hud-ckui-pull-right uptime]]
    ))

(defn view []
  [:div.hud-ckw-system
   [render-row-uptime]
   ;; TODO: Location should be a widget of its own
   [:div.hud-ckui-row
    [:span.hud-ckui-label.hud-ckui-fill-1 "Location:"]
    [:span.hud-ckui-item.hud-ckui-pull-right "Santo André, Brasil"]]

   ])
