(ns web.apps.log-viewer.popups.log-edit.view
  (:require [he.core :as he]
            [re-com.core :refer [single-dropdown]]))

(defn on-type-selection
  [app-id selected-type]
  ;; Clear existing fields
  (let [app (.getElementById js/document app-id)
        inputs (array-seq (.querySelectorAll app ".lv-led-edit-field-input"))]
    (doseq [el-input inputs]
      (set! (.-value el-input) "")))
  (he/dispatch
   [:web|apps|log-viewer|log-edit|on-type-selection app-id selected-type]))

(defn log-type-dropdown
  [app-id]
  (let [dropdown-map (he/subscribe [:game|server|log|dropdown-map])
        log-type (he/subscribe [:web|apps|log-viewer|log-edit|log|type app-id])]
    [single-dropdown
     :choices dropdown-map
     :width "250px"
     :max-height "400px"
     :model log-type
     :on-change #(on-type-selection app-id %)
     :filter-box? true]))

(defn render-preview-area
  [app-id]
  (let [preview (he/subscribe [:web|apps|log-viewer|log-edit|log|html app-id])
        corrupt? (he/subscribe [:web|apps|log-viewer|log-edit|corrupt? app-id])]
    [:div.lv-led-preview-area
     [:fieldset
      {:class (when corrupt?
                [:lv-led-preview-area-corrupt])}
      [:legend "Preview"]
      [:div.lv-led-preview-wrapper
       [:div.lv-led-preview-icon
        (if corrupt?
          [:i.fa.fa-exclamation-triangle.lv-led-preview-icon-corrupt
           {:tip "This log will be shown as corrupt"}]
          [:i.far.fa-check-circle.lv-led-preview-icon-success
           {:tip "This log is valid!"}])]
       [:span.lv-led-preview-text preview]]]]))

(defn on-field-change
  [app-id field-id field-type event]
  (he/dispatch
   [:web|apps|log-viewer|log-edit|on-field-change
    app-id field-id field-type (-> event .-target .-value)]))

(defn on-field-row-click
  [input-id]
  (.focus (js/document.getElementById input-id)))

(defn render-field-row
  [app-id field-id field field-value]
  (let [valid? (he/subscribe
                [:web|apps|log-viewer|log-edit|field-valid? app-id field-id])
        {type :type desc :desc help :help} field
        input-id (str app-id "-" (name field-id))]
    [:div.lv-led-edit-field-row
     {:on-click #(on-field-row-click input-id)}
     [:div.lv-led-edit-field-help.ui-help
      {:tip (:help field)}
      [:i.far.fa-question-circle]]
     [:label.lv-led-edit-field-text (:desc field)]
     [:div.lv-led-edit-field-input-area
      [:input.lv-led-edit-field-input.ui-input
       {:id input-id
        :class (when-not valid?
                 [:field-content-invalid])
        :type :text
        :spellCheck false
        :defaultValue field-value
        :on-change #(on-field-change app-id field-id (:type field) %)}]
      (when-not valid?
        [:i.fa.fa-exclamation-triangle
         {:tip (:validator-help field)}])]]))

(defn render-edit-fields
  [app-id]
  (let [log-type (he/subscribe [:web|apps|log-viewer|log-edit|log|type app-id])
        log-data (he/subscribe [:web|apps|log-viewer|log-edit|log|data app-id])
        fields-info (he/subscribe [:game|server|log|fields-data log-type])
        fields (:fields fields-info)]
    (if-not (empty? fields)
      [:div.lv-led-edit-field-rows
       (for [[field-id field] fields]
         (let [key (str app-id "-" field-id)
               field-value (get log-data field-id)]
           ^{:key key} [render-field-row app-id field-id field field-value]))]
      [:div.lv-led-edit-field-empty
       "This log does not have custom fields"])))

(defn render-edit-fields-container
  [app-id]
  [:div.lv-led-edit-fields
   [:fieldset
    [:legend "Fields"]
    [render-edit-fields app-id]]])

(defn render-edit-area
  [app-id]
  [:div.lv-led-edit-area
   [:div.lv-led-edit-type
    [:div.lv-led-edit-type-help.ui-help
     [:i.far.fa-question-circle]]
    [:span "Log type"]
    [:div.lv-led-edit-type-select
     [log-type-dropdown app-id]]]
   [render-edit-fields-container app-id]])

(defn view-body
  [app-id]
  [:div.lv-led-body
   [render-preview-area app-id]
   [:div.lv-led-section-separator]
   [render-edit-area app-id]])

(defn view-footer
  [app-id]
  [:div.lv-led-footer.ui-app-footer
   [:div.lv-led-alternative-buttons
    [:button.ui-btn.btn-icon
     {:tip "Delete the log"}
     [:i.fa.fa-trash-alt]]
    [:button.ui-btn.btn-icon
     {:tip "Hide"}
     [:i.far.fa-eye-slash]]
    [:button.ui-btn.btn-icon
     {:tip "Search for previous revisions"}
     [:i.fa.fa-question-circle]]]
   [:div.lv-led-main-buttons
    [:button.ui-btn.btn-dual
     [:i.far.fa-times-circle]
     [:span "Cancel"]]
    [:button.ui-btn.btn-dual.btn-primary.lv-led-button-edit
     {:tip "Update the log contents"
      :on-click #(he/dispatch [:web|apps|log-viewer|log-edit|log|edit app-id])}
     [:i.fa.fa-edit]
     [:span "Edit"]]]])

(defn ^:export view
  [app-id server-cid]
  [:div.lv-led-container
   [view-body app-id]
   [view-footer app-id]])
