(ns web.apps.file-explorer.view
  (:require [he.core :as he]))

(def sort-flags
  [{:id :module-one
    :icon "fas fa-sort-amount-up"
    :tip "Sort files by first module version"}
   {:id :module-two
    :icon "fas fa-sort-amount-down"
    :tip "Sort files by second module version"}
   {:id :az-asc
    :icon "fas fa-sort-alpha-up"
    :tip "Sort files alphabetically (A-Z)"}
   {:id :az-desc
    :icon "fas fa-sort-alpha-down"
    :tip "Sort files alphabetically (Z-A)"}])

(def view-mode-data
  [{:id :tree
    :icon "fas fa-tree"
    :tip "Switch to Tree View mode"
    :class "a-fe-sb-view-tree"}
   {:id :flat
    :icon "far fa-list-alt"
    :tip "Switch to Flat View mode"
    :class "a-fe-sb-view-flat"}])

(def file-action-data
  {:execute {:icon "fas fa-terminal"
             :tip "Execute this file"
             :class "a-fe-b-file-action-execute"}
   :upload {:icon "fas fa-upload"
            :tip "Upload this file"
            :class "a-fe-b-file-action-upload"}
   :download {:icon "fas fa-download"
              :tip "Download this file"
              :class "a-fe-b-file-action-download"}
   :delete {:icon "fa fa-trash-alt"
            :tip "Delete this file"
            :class "a-fe-b-file-action-delete"}})

(defn on-file-click
  [app-id file-id _event]
  (he/dispatch [:web|apps|file-explorer|file-click app-id file-id]))

(defn generic-module-renderer
  [module-number file renderer-fn]
  (let [module-info (get-in file [:module-meta (keyword module-number)])
        version (get-in file [:modules (:id module-info) :display-version])]
    (when-not (nil? module-info)
      (renderer-fn version (:icon module-info) (:name module-info)))))

(defn render-file-module-entry
  [version icon]
  [:div.a-fe-b-file-module
   [:div.a-fe-b-file-module-icon
    [:i {:class icon}]]
   [:div.a-fe-b-file-module-version
    [:span version]]])

(defn render-file-module
  [file]
  [:div.a-fe-b-file-module-area
   [generic-module-renderer "one" file render-file-module-entry]
   [generic-module-renderer "two" file render-file-module-entry]])

(defn render-file
  [app-id file-id file]
  [:div.a-fe-b-file
   {:on-click #(on-file-click app-id file-id %)}
   [:div.a-fe-b-file-icon
    [:i {:class (:icon file)}]]
   [:div.a-fe-b-file-name
    [:span (:display-name file)]]
   [render-file-module file]])

(defn render-selected-file-module
  [version icon name]
  [:div.a-fe-b-file-selected-module
   [:div.a-fe-b-file-selected-module-name
    [:span name]]
   [:div.a-fe-b-file-selected-module-version
    [:span version]]
   [:div.a-fe-b-file-selected-module-icon
    [:i {:class icon}]]])

(defn render-selected-file-side-entry
  [text icon-class]
  [:div.a-fe-b-file-selected-side-entry
   [:div.a-fe-b-file-selected-side-entry-icon
    [:i {:class icon-class}]]
   [:div.a-fe-b-file-selected-side-entry-text
    [:span text]]])

(defn render-selected-file-side
  [file]
  [:div.a-fe-b-file-selected-side
   [render-selected-file-side-entry (:size file) "fas fa-weight-hanging"]
   [render-selected-file-side-entry (:license file) "fas fa-balance-scale"]])

(defn render-selected-file-modules
  [file]
  [:div.a-fe-b-file-selected-module-area
   [generic-module-renderer "one" file render-selected-file-module]
   [generic-module-renderer "two" file render-selected-file-module]
   [render-selected-file-side file]])

(defn on-selected-file-action-click
  [app-id file-info action-id event]
  (he/dispatch [:web|apps|file-explorer|file-action app-id file-info action-id])
  (.stopPropagation event))

(defn render-selected-file-action
  [app-id file-info action-id]
  (let [action-info (get file-action-data action-id)]
    [:button.ui-btn.ui-btn-icon
     {:class (:class action-info)
      :tip (:tip action-info)
      :on-click #(on-selected-file-action-click app-id file-info action-id %)}
     [:i {:class (:icon action-info)}]]))

(defn render-selected-file-actions
  [app-id file-info]
  (let [context (he/subscribe [:web|apps|context app-id])
        session-id (he/subscribe [:web|wm|active-session])
        session (he/subscribe [:web|wm|current-session])
        transfer-action (cond
                          (= context :remote) :download
                          (not (nil? (:endpoint session))) :upload
                          :else nil)]
    [:div.a-fe-b-file-selected-action-area.ui-btn-area-large
     [render-selected-file-action app-id file-info :execute]
     (when transfer-action
       [render-selected-file-action app-id file-info transfer-action])
     [render-selected-file-action app-id file-info :delete]]))

(defn render-selected-file
  [app-id file-id file]
  [:div.a-fe-b-file-selected
   {:on-click #(on-file-click app-id file-id %)}
   [:div.a-fe-b-file-selected-top
    [:div.a-fe-b-file-selected-icon
     [:i {:class (:icon file)}]]
    [:div.a-fe-b-file-selected-name
     [:span (:display-name file)]]
    [:div.a-fe-b-file-selected-type
     [:span (:software-name file)]]]
   [render-selected-file-modules file]
   [:div.a-fe-b-file-selected-separator]
   [render-selected-file-actions app-id [(:type file) file-id]]])

(defn render-file-entries
  [app-id server-cid]
  (let [storage-id (he/subscribe [:web|apps|file-explorer|storage-id app-id])
        selected-id (he/subscribe
                     [:web|apps|file-explorer|selected-file app-id])
        files (he/subscribe
               [:web|apps|file-explorer|entries app-id server-cid storage-id])]
    [:div.a-fe-b-file-entries
     (for [[file-id file] files]
       (let [key (str app-id "-" file-id)]
         (if (= file-id selected-id)
           ^{:key key} [render-selected-file app-id file-id file]
           ^{:key key} [render-file app-id file-id file])))]))

(defn render-flat-view
  [app-id server-cid]
  [:div.a-fe-b-flat-view
   [render-file-entries app-id server-cid]])

(defn render-body
  [app-id server-cid]
  (let [view-mode (he/subscribe [:web|apps|file-explorer|view-mode app-id])]
    [:div.a-fe-body
     (if (= view-mode :flat)
       [render-flat-view app-id server-cid]
       [:div "Todo"])]))

(defn on-flag-sort-click
  [app-id sort-id _event]
  (he/dispatch [:web|apps|file-explorer|sort-click app-id sort-id]))

(defn render-flag-sort
  [app-id sort-info enabled?]
  [:div.a-fe-h-flag
   {:tip (:tip sort-info)
    :on-click #(on-flag-sort-click app-id (:id sort-info) %)
    :class (when-not enabled?
             "a-fe-h-flag-disabled")}
   [:i {:class (:icon sort-info)}]])

(defn render-flag-area
  [app-id]
  (let [sort-order (he/subscribe [:web|apps|file-explorer|sort-order app-id])]
    [:div.a-fe-h-flag-area
     (for [sort-info sort-flags]
       (let [key (str app-id "-" (:id sort-info))
             enabled? (= (:id sort-info) sort-order)]
         ^{:key key} [render-flag-sort app-id sort-info enabled?]))]))

(defn render-search-area []
  [:div.a-fe-h-search-area
   [:div.a-fe-h-search-filter
    {:tip "Advanced filter"}
    [:i.fa.fa-filter]]
   [:div.a-fe-h-search-input
    [:input.ui-input
     {:placeholder "Search"}]
    [:i.fa.fa-search
     {:tip "Search for a specific file"}]]])

(defn render-header
  [app-id]
  [:div.a-fe-header
   [render-flag-area app-id]
   [render-search-area]])

(defn render-storage-entry
  [app-id server-cid storage-id selected?]
  (let [storage (he/subscribe
                 [:game|server|software|storage|info server-cid storage-id])]
    [:div.a-fe-sb-storage-entry
     {:class (when selected?
               :fe-storage-entry-selected)}
     [:div.a-fe-sb-storage-entry-icon
      [:i.far.fa-hdd]]
     [:div.a-fe-sb-storage-entry-desc
      [:div.a-fe-sb-storage-entry-desc-name
       [:span (:name storage)]]
      [:div.a-fe-sb-storage-entry-desc-info
       [:span "0.1/10GB (1%)"]]]]))

(defn render-sidebar-storages
  [app-id server-cid]
  (let [storages (he/subscribe [:game|server|software|storage-ids server-cid])
        selected-id (he/subscribe [:web|apps|file-explorer|storage-id app-id])]
    [:div.a-fe-sb-storage-area
     (for [storage-id storages]
       (let [key (str app-id "-" storage-id)
             selected? (= storage-id selected-id)]
         ^{:key key}
         [render-storage-entry app-id server-cid storage-id selected?]))]))

(defn render-sidebar-actions
  [app-id]
  [:div.a-fe-sb-action-area
   [:div.a-fe-sb-action-button
    [:button.ui-btn "Actions"]]])

(defn on-selector-click
  [app-id view-id]
  (when (= view-id :tree)
    (he.error/not-implemented "TreeView is TODO.")))

(defn render-sidebar-view-selector
  [app-id info active?]
  [:div
   {:class (if active?
             (str (:class info) " a-fe-sb-view-selected")
             (:class info))
    :tip (:tip info)
    :on-click #(on-selector-click app-id (:id info))}
   [:i {:class (:icon info)}]])

(defn render-sidebar-view
  [app-id]
  (let [active-view (he/subscribe [:web|apps|file-explorer|view-mode app-id])]
    [:div.a-fe-sb-view-area
     (for [data view-mode-data]
       (let [key (str app-id "-" (:id data))
             active? (= (:id data) active-view)]
         ^{:key key} [render-sidebar-view-selector app-id data active?]))]))

(defn render-sidebar
  [app-id server-cid]
  [:div.a-fe-sidebar
   [render-sidebar-storages app-id server-cid]
   [render-sidebar-actions app-id]
   [render-sidebar-view app-id]])

(defn ^:export view
  [app-id server-cid]
  [:div.a-fe-container
   [render-sidebar app-id server-cid]
   [:div.a-fe-main
    [render-header app-id]
    [render-body app-id server-cid]]])
