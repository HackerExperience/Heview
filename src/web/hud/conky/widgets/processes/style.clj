(ns web.hud.conky.widgets.processes.style
  (:require [web.ui.vars :as ui]))

(defn style []
  [[:.hud-ckw-prc-timed-pbar
    {:width "120px"}]
   [:.hud-ckw-prc-timed-resources
    {:padding-left "5px"
     :flex "1 1"
     :display :flex
     :flex-direction :row
     :justify-content :space-between}]
   [:.hud-ckw-prc-timed-resource-entry-icon
    {:font-size "11px"
     :padding-right "3px"}]
   [:.hud-ckw-prc-separator
    {:margin "2px 0"
     :height "1px"
     :border-top (str "1px dotted " ui/color-primary-darker)}]
   [:.hud-ckw-prc-recur-resource-icon
    {:font-size "11px"
     :padding-right "3px"}]
   [:.hud-ckw-prc-recur-resource-text
    {:padding-right "3px"}]])
