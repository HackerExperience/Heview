(ns web.apps.software.cracker.view
  (:require [he.core :as he]
            [web.ui.components :as ui.components]))

(def tab-definition
  [{:id :bruteforce :text "Bruteforce" :icon "fa fa-archive"}
   {:id :overflow :text "Overflow" :icon "fab fa-windows"}])

(defn render-overflow
  [app-id]
  [:div "ov"])

(defn render-bruteforce-field-target
  [app-id]
  (let [ip (he/subscribe [:web|apps|software|cracker|bruteforce|ip app-id])]
    [:div.a-crc-bf-b-field
     [:div.a-crc-bf-b-field-help.ui-help
      {:tip "IP address of the server you want to crack"}
      [:i.far.fa-question-circle]]
     [:div.a-crc-bf-b-field-label
      [:span "Target"]]
     [:div.a-crc-bf-b-field-input-area
      [:input.a-crc-bf-b-field-input.ui-input
       {:value ip}]]]))

;; Feature idea: Add a new tab to Database -> "Saved IPs"
;; Any IP the user founds, she can hover and click "Save to DB"
;; Fields like the `target-ip` below can then select from saved IPs, like the
;; Bounce dropdown

(defn render-bruteforce-body
  [app-id]
  [:div.a-crc-bf-body
   [:div.a-crc-bf-b-fields
    [render-bruteforce-field-target app-id]
    [:div.a-crc-bf-b-field
     [:div.ui-help
      {:tip "Bounce that will be used to mask your gateway IP"}
      [:i.far.fa-question-circle]]
     [:div.a-crc-bf-b-field-label
      [:span "Bounce"]]
     [:div.a-crc-bf-b-field-input-area
      [:input.a-crc-bf-b-field-input.ui-input]]]]])

(defn render-bruteforce-footer
  [app-id]
  [:div.a-crc-bf-footer
   [:div.a-crc-bf-f-action-area
    [:div.a-crc-bf-f-action-left
     [:button.ui-btn.ui-btn-dual
      [:i.fa.fa-archive]
      [:span "Cancel"]]]
    [:div.a-crc-bf-f-action-right
     [:button.ui-btn.ui-btn-dual.ui-btn-primary
      {:tip "Start the Bruteforce process against the target"
       :on-click #(he/dispatch [:web|apps|software|cracker|bruteforce app-id])}
      [:i.fa.fa-archive]
      [:span "Start"]]]]])

(defn render-bruteforce
  [app-id]
  [:div.a-crc-bf-container
   [render-bruteforce-body app-id]
   [render-bruteforce-footer app-id]])

(defn render-body
  [app-id active-tab]
  (if (= :overflow active-tab)
    [render-overflow app-id]
    [render-bruteforce app-id]))

(defn on-tab-click
  [app-id tab-id _event]
  (he/dispatch [:web|apps|software|cracker|set-tab app-id tab-id]))

(defn render-header
  [app-id active-tab]
  (let [on-tab-click (partial on-tab-click app-id)]
    [ui.components/tab on-tab-click tab-definition active-tab]))

(defn ^:export view
  [app-id server-cid file-id]
  (let [active-tab (he/subscribe [:web|apps|software|cracker|tab app-id])]
    [:div.a-crc-container
     [render-header app-id active-tab]
     [render-body app-id active-tab]]))
