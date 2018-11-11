(ns web.apps.log-viewer.view
  (:require [he.core :as he]))

(defn increment-counter
  [app-id]
  [:button {:on-click #(he/dispatch [:web|apps|log-viewer|inc app-id])} "+"])

(defn decrement-counter
  [app-id]
  [:button {:on-click #(he/dispatch [:web|apps|log-viewer|dec app-id])} "-"])

(defn show-state
  [app-id]
  (let [counter (he/subscribe [:web|apps|log-viewer|counter app-id])]
    [:span (str counter)]))

(defn ^:export view
  [app-id]
  [:div (show-state app-id)
   [increment-counter app-id]
   [decrement-counter app-id]])
