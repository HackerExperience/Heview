(ns web.ui.components
  (:require [reagent.core :as r]
            [clojure.string :as str]
            [web.ui.components.impl.dropdown.view :as impl.dropdown]))

;; Tab

(defn tab-entry
  [on-click-fn entry active-tab]
  [:div.ui-tab-entry
   {:class (when (= (:id entry) active-tab)
             :ui-tab-selected)
    :on-click #(on-click-fn (:id entry) %)}
   [:div.ui-tab-entry-icon
    [:i {:class (:icon entry)}]]
   [:div.ui-tab-entry-text
    [:span (:text entry)]]])

(defn tab
  [on-click-fn entries active-tab]
  [:div.ui-tab-area
   [:div.ui-tab-pre]
   (for [entry entries]
     (let [key (int (* 10000 (.random js/Math)))]
       ^{:key key} [tab-entry on-click-fn entry active-tab]))
   [:div.ui-tab-rest]])

;; Dropdown

(defn dropdown
  [opts]
  (println "Ueh")
  (impl.dropdown/dropdown opts))
