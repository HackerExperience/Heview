(ns web.hud.conky.ui)

(defn progress-bar
  [percentage]
  [:div.hud-ckui-bar
   [:div.hud-ckui-bar-progress
    {:style {:width percentage}}]])

(defn button
  [text]
  [:div.hud-ckui-button text])

(defn widget-header-line
  [text]
  [:div.hud-ckui-row
   [:div.hud-ckui-label text]
   [:div.hud-ckui-header-line.hud-ckui-fill-1]])

;; (defn row
;;   [contents]
;;   [:div.hud-ckui-row
;;    contents])
