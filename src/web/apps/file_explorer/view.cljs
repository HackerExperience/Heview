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
    :class "fe-sidebar-view-tree"}
   {:id :flat
    :icon "far fa-list-alt"
    :tip "Switch to Flat View mode"
    :class "fe-sidebar-view-flat"}])

(def file-action-data
  {:execute {:icon "fas fa-terminal"
             :tip "Execute this file"
             :class "fe-file-action-execute"}
   :upload {:icon "fas fa-upload"
            :tip "Upload this file"
            :class "fe-file-action-upload"}
   :download {:icon "fas fa-download"
              :tip "Download this file"
              :class "fe-file-action-download"}
   :delete {:icon "fa fa-trash-alt"
            :tip "Delete this file"
            :class "fe-file-action-delete"}})

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
  [:div.fe-file-module
   [:div.fe-file-module-icon
    [:i {:class icon}]]
   [:div.fe-file-module-version
    [:span version]]])

(defn render-file-module
  [file]
  [:div.fe-file-module-area
   [generic-module-renderer "one" file render-file-module-entry]
   [generic-module-renderer "two" file render-file-module-entry]])

(defn render-file
  [app-id file-id file]
  [:div.fe-file
   {:on-click #(on-file-click app-id file-id %)}
   [:div.fe-file-icon
    [:i {:class (:icon file)}]]
   [:div.fe-file-name
    [:span (:display-name file)]]
   [render-file-module file]])

(defn render-selected-file-module
  [version icon name]
  [:div.fe-file-selected-module
   [:div.fe-file-selected-module-name
    [:span name]]
   [:div.fe-file-selected-module-version
    [:span version]]
   [:div.fe-file-selected-module-icon
    [:i {:class icon}]]])

(defn render-selected-file-side-entry
  [text icon-class]
  [:div.fe-file-selected-side-entry
   [:div.fe-file-selected-side-entry-icon
    [:i {:class icon-class}]]
   [:div.fe-file-selected-side-entry-text
    [:span text]]])

(defn render-selected-file-side
  [file]
  [:div.fe-file-selected-side
   [render-selected-file-side-entry (:size file) "fas fa-weight-hanging"]
   [render-selected-file-side-entry (:license file) "fas fa-balance-scale"]])

(defn render-selected-file-modules
  [file]
  [:div.fe-file-selected-module-area
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
    [:button.ui-btn.btn-icon
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
    [:div.fe-file-selected-action-area.ui-btn-area-large
     [render-selected-file-action app-id file-info :execute]
     (when transfer-action
       [render-selected-file-action app-id file-info transfer-action])
     [render-selected-file-action app-id file-info :delete]]))

(defn render-selected-file
  [app-id file-id file]
  (println file)
  [:div.fe-file-selected
   {:on-click #(on-file-click app-id file-id %)}
   [:div.fe-file-selected-top
    [:div.fe-file-selected-icon
     [:i {:class (:icon file)}]]
    [:div.fe-file-selected-name
     [:span (:display-name file)]]
    [:div.fe-file-selected-type
     [:span (:software-name file)]]]
   [render-selected-file-modules file]
   [:div.fe-file-selected-separator]
   [render-selected-file-actions app-id [(:type file) file-id]]])

(defn render-file-entries
  [app-id server-cid]
  (let [storage-id (he/subscribe [:web|apps|file-explorer|storage-id app-id])
        selected-id (he/subscribe
                     [:web|apps|file-explorer|selected-file app-id])
        files (he/subscribe
               [:web|apps|file-explorer|entries app-id server-cid storage-id])]
    [:div.fe-file-entries
     (for [[file-id file] files]
       (let [key (str app-id "-" file-id)]
         (if (= file-id selected-id)
           ^{:key key} [render-selected-file app-id file-id file]
           ^{:key key} [render-file app-id file-id file])))]))

(defn render-flat-view
  [app-id server-cid]
  [:div.fe-flat-view
   [render-file-entries app-id server-cid]])

(defn render-body
  [app-id server-cid]
  (let [view-mode (he/subscribe [:web|apps|file-explorer|view-mode app-id])]
    [:div.fe-body
     (if (= view-mode :flat)
       [render-flat-view app-id server-cid]
       [:div "Todo"])]))

(defn on-flag-sort-click
  [app-id sort-id _event]
  (he/dispatch [:web|apps|file-explorer|sort-click app-id sort-id]))

(defn render-flag-sort
  [app-id sort-info enabled?]
  [:div.fe-flag
   {:tip (:tip sort-info)
    :on-click #(on-flag-sort-click app-id (:id sort-info) %)
    :class (when-not enabled?
             "fe-flag-disabled")}
   [:i {:class (:icon sort-info)}]])

(defn render-flag-area
  [app-id]
  (let [sort-order (he/subscribe [:web|apps|file-explorer|sort-order app-id])]
    [:div.fe-flag-area
     (for [sort-info sort-flags]
       (let [key (str app-id "-" (:id sort-info))
             enabled? (= (:id sort-info) sort-order)]
         ^{:key key} [render-flag-sort app-id sort-info enabled?]))]))

(defn render-search-area []
  [:div.fe-search-area
   [:div.fe-search-filter
    {:tip "Advanced filter"}
    [:i.fa.fa-filter]]
   [:div.fe-search-input
    [:input.ui-input
     {:placeholder "Search"}]
    [:i.fa.fa-search
     {:tip "Search for a specific file"}]]])

(defn render-header
  [app-id]
  [:div.fe-header
   [render-flag-area app-id]
   [render-search-area]])

(defn render-storage-entry
  [app-id server-cid storage-id selected?]
  (let [storage (he/subscribe
                 [:game|server|software|storage|info server-cid storage-id])]
    [:div.fe-storage-entry
     {:class (when selected?
               :fe-storage-entry-selected)}
     [:div.fe-storage-entry-icon
      [:i.far.fa-hdd]]
     [:div.fe-storage-entry-desc
      [:div.fe-storage-entry-desc-name
       [:span (:name storage)]]
      [:div.fe-storage-entry-desc-info
       [:span "0.1/10GB (1%)"]]]]))

(defn render-sidebar-storages
  [app-id server-cid]
  (let [storages (he/subscribe [:game|server|software|storage-ids server-cid])
        selected-id (he/subscribe [:web|apps|file-explorer|storage-id app-id])]
    [:div.fe-sidebar-storage-area
     (for [storage-id storages]
       (let [key (str app-id "-" storage-id)
             selected? (= storage-id selected-id)]
         ^{:key key}
         [render-storage-entry app-id server-cid storage-id selected?]))]))

(defn render-sidebar-actions
  [app-id]
  [:div.fe-sidebar-action-area
   [:div.fe-sidebar-action-button
    [:button.ui-btn "Actions"]]])

(defn on-selector-click
  [app-id view-id]
  (when (= view-id :tree)
    (he.error/not-implemented "TreeView is TODO.")))

(defn render-sidebar-view-selector
  [app-id info active?]
  [:div
   {:class (if active?
             (str (:class info) " fe-sidebar-view-selected")
             (:class info))
    :tip (:tip info)
    :on-click #(on-selector-click app-id (:id info))}
   [:i {:class (:icon info)}]])

(defn render-sidebar-view
  [app-id]
  (let [active-view (he/subscribe [:web|apps|file-explorer|view-mode app-id])]
    [:div.fe-sidebar-view-area
     (for [data view-mode-data]
       (let [key (str app-id "-" (:id data))
             active? (= (:id data) active-view)]
         ^{:key key} [render-sidebar-view-selector app-id data active?]))]))

(defn render-sidebar
  [app-id server-cid]
  [:div.fe-sidebar
   [render-sidebar-storages app-id server-cid]
   [render-sidebar-actions app-id]
   [render-sidebar-view app-id]])

(defn ^:export view
  [app-id server-cid]
  [:div.fe-container
   [render-sidebar app-id server-cid]
   [:div.fe-main
    [render-header app-id]
    [render-body app-id server-cid]]])
