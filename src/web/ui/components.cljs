(ns web.ui.components)

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
