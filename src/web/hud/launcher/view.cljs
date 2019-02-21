(ns web.hud.launcher.view
  (:require [he.core :as he]))

(defn on-overlay-app-click
  [event]
  (he/dispatch event)
  (he/dispatch [:web|hud|launcher|close-overlay]))

(defn render-overlay-app
  [{icon-class :icon-class name :name event :on-click-event}]
  [:div.hud-la-overlay-grid-entry
   {:on-click #(on-overlay-app-click event)}
   [:i
    {:class icon-class}]
   [:span name]])

(defn launcher-overlay []
  (let [apps (he/subscribe [:web|hud|launcher|config])]
    [:div.hud-la-overlay
     [:div.hud-la-overlay-area
      [:div.hud-la-overlay-input-area
       [:input#hud-la-overlay-input.la-overlay-input.ui-input
        {:placeholder "Search app..."}]]
      [:div.hud-la-overlay-grid
       (for [app apps]
         ^{:key (str "overlay-app-" (:name app))} [render-overlay-app app])]]]))

(defn open-overlay-fn
  [event]
  (he/dispatch [:web|hud|launcher|open-overlay])
  ;; Focus the (soon-to-be-rendered) overlay input
  (js/setTimeout
   #(.focus (js/document.getElementById "hud-la-overlay-input"))
   50)
  (.stopPropagation event))

(defn view []
  (let [show-overlay? (he/subscribe [:web|hud|launcher|show-overlay?])]
  [:div#hud-launcher
   {:on-click #(he/dispatch [:web|hud|launcher|close-overlay])}
   (when show-overlay?
     [launcher-overlay])
   [:div.hud-la-button
    {:on-click #(open-overlay-fn %)}
    [:i.fas.fa-satellite-dish]]]))
