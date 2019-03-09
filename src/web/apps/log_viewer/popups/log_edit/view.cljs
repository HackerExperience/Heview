(ns web.apps.log-viewer.popups.log-edit.view
  (:require [he.core :as he]
            [web.ui.components :as ui.components]))

(defn on-type-selection
  [app-id selected-type]
  ;; Clear existing fields
  (let [app (.getElementById js/document app-id)
        inputs (array-seq (.querySelectorAll app ".a-lv-led-b-edit-field-input"))]
    (doseq [el-input inputs]
      (set! (.-value el-input) "")))
  (he/dispatch
   [:web|apps|log-viewer|log-edit|on-type-selection app-id selected-type]))

(defn log-type-dropdown
  [app-id]
  (let [dropdown-map (he/subscribe [:game|server|log|dropdown-map])
        log-type (he/subscribe [:web|apps|log-viewer|log-edit|log|type app-id])]
    [ui.components/dropdown
     {:entries dropdown-map
      :entry-id log-type
      :on-change #(on-type-selection app-id %)
      :class-prefix :a-lv-led-b-edit-type
      :grouped? true}]))

(defn render-preview-area
  [app-id]
  (let [preview (he/subscribe [:web|apps|log-viewer|log-edit|log|html app-id])
        corrupt? (he/subscribe [:web|apps|log-viewer|log-edit|corrupt? app-id])]
    [:div.a-lv-led-b-preview-area
     [:fieldset
      {:class (when corrupt?
                [:a-lv-led-b-preview-area-corrupt])}
      [:legend "Preview"]
      [:div.a-lv-led-b-preview-wrapper
       [:div.a-lv-led-b-preview-icon
        (if corrupt?
          [:i.fa.fa-exclamation-triangle.a-lv-led-b-preview-icon-corrupt
           {:tip "This log will be shown as corrupt"}]
          [:i.far.fa-check-circle.a-lv-led-b-preview-icon-success
           {:tip "This log is valid!"}])]
       [:span.a-lv-led-b-preview-text preview]]]]))

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
    [:div.a-lv-led-b-edit-field-row
     {:on-click #(on-field-row-click input-id)}
     [:div.a-lv-led-b-edit-field-help.ui-help
      {:tip (:help field)}
      [:i.far.fa-question-circle]]
     [:label.a-lv-led-b-edit-field-text (:desc field)]
     [:div.a-lv-led-b-edit-field-input-area
      [:input.a-lv-led-b-edit-field-input.ui-input
       {:id input-id
        :class (when-not valid?
                 [:a-lv-led-b-edit-field-content-invalid])
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
      [:div.a-lv-led-b-edit-field-rows
       (for [[field-id field] fields]
         (let [key (str app-id "-" field-id)
               field-value (get log-data field-id)]
           ^{:key key} [render-field-row app-id field-id field field-value]))]
      [:div.a-lv-led-b-edit-field-empty
       "This log does not have custom fields"])))

(defn render-edit-fields-container
  [app-id]
  [:div.a-lv-led-b-edit-fields
   [:fieldset
    [:legend "Fields"]
    [render-edit-fields app-id]]])

(defn render-edit-area
  [app-id]
  [:div.a-lv-led-b-edit-area
   [:div.a-lv-led-b-edit-type
    [:div.a-lv-led-b-edit-type-help.ui-help
     [:i.far.fa-question-circle]]
    [:span "Log type"]
    [:div.a-lv-led-b-edit-type-select
     [log-type-dropdown app-id]]]
   [render-edit-fields-container app-id]])

(defn view-body
  [app-id]
  [:div.a-lv-led-body
   [render-preview-area app-id]
   [:div.a-lv-led-b-section-separator]
   [render-edit-area app-id]])

(defn view-footer
  [app-id]
  [:div.a-lv-led-footer.ui-app-footer
   [:div.a-lv-led-f-alternative-buttons
    [:button.ui-btn.ui-btn-icon
     {:tip "Delete the log"}
     [:i.fa.fa-trash-alt]]
    [:button.ui-btn.ui-btn-icon
     {:tip "Hide"}
     [:i.far.fa-eye-slash]]
    [:button.ui-btn.ui-btn-icon
     {:tip "Search for previous revisions"}
     [:i.fa.fa-question-circle]]]
   [:div.a-lv-led-f-main-buttons
    [:button.ui-btn.ui-btn-dual
     [:i.far.fa-times-circle]
     [:span "Cancel"]]
    [:button.ui-btn.ui-btn-dual.ui-btn-primary.a-lv-led-f-button-edit
     {:tip "Update the log contents"
      :on-click #(he/dispatch [:web|apps|log-viewer|log-edit|log|edit app-id])}
     [:i.fa.fa-edit]
     [:span "Edit"]]]])

(defn ^:export view
  [app-id server-cid]
  [:div.a-lv-led-container
   [view-body app-id]
   [view-footer app-id]])
