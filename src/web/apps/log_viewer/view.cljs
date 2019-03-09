(ns web.apps.log-viewer.view
  (:require [he.core :as he]
            [web.apps.log-viewer.popups.log-edit.view]))

(defn render-flag-area []
  [:div.a-lv-h-flag-area
   [:div.a-lv-h-flag
    {:tip "Show/hide logs that were edited by you"}
    [:i.fa.fa-user]]
   [:div.a-lv-h-flag.a-lv-h-flag-disabled
    {:tip "Show/hide logs that contain your IP address"}
    [:i.fa.fa-archive]]
   [:div.a-lv-h-flag
    {:tip "Show/hide bounce logs"}
    [:i.fa.fa-search]]])

(defn render-search-area []
  [:div.a-lv-h-search-area
   [:div.a-lv-h-search-filter
    {:tip "Advanced filter"}
    [:i.fa.fa-filter]]
   [:div.a-lv-h-search-input
    [:input.ui-input
     {:placeholder "Search"}]
    [:i.fa.fa-search
     {:tip "Search the logs for a specific word"}]]])

(defn render-entry
  [log-id log]
  [:div.a-lv-b-entry
   [:div.a-lv-b-entry-date
    [:div.a-lv-b-entry-date-dmy "26/01/2019"]
    [:div.a-lv-b-entry-date-hms "19:29:18.123"]]
   [:div.a-lv-b-entry-separator]
   [:div.a-lv-b-entry-body
    (:html log)]])

(defn on-click-edit-log
  [app-id server-cid log-id event]
  (.stopPropagation event)
  (.preventDefault event)
  (he/dispatch
   [:web|wm|app|open-popup
    [:log-viewer :log-edit app-id]
    [server-cid log-id]]))

(defn render-selected-entry
  [app-id server-cid log-id log]
  [:div.a-lv-b-selected-container
   [render-entry log-id log]
   [:div.a-lv-b-selected-separator]
   [:div.a-lv-b-selected-action-area.ui-btn-area-large
    [:button.ui-btn.ui-btn-icon.a-lv-b-selected-action-edit
     {:tip "Edit the log contents"
      :on-click #(on-click-edit-log app-id server-cid log-id %)}
     [:i.fa.fa-edit]]
    [:button.ui-btn.ui-btn-icon.a-lv-b-selected-action-search
     {:tip "Search for previous revisions"}
     [:i.fa.fa-search]]
    [:button.ui-btn.ui-btn-icon.a-lv-b-selected-action-delete
     {:tip "Delete the log entry"}
     [:i.fa.fa-trash-alt]]]])

(defn add-entry-events
  [app-id log-id]
  {:on-click
   #(he/dispatch [:web|apps|log-viewer|on-entry-click app-id log-id])})

(defn list-entries
  [app-id server-cid]
  (let [entries (he/subscribe [:web|apps|log-viewer|entries server-cid])
        selected-id (he/subscribe [:web|apps|log-viewer|selected-entry app-id])]
    [:div.a-lv-b-entries
     (for [[log-id log] entries]
       ^{:key log-id} [:div.a-lv-b-entry-container
                       (add-entry-events app-id log-id)
                       (if (= log-id selected-id)
                         [render-selected-entry app-id server-cid log-id log]
                         [render-entry log-id log])])]))

(defn render-action-area
  [app-id server-cid]
  [:div.a-lv-action-area.ui-btn-area
   [:button.ui-btn.ui-btn-dual
    {:tip "Forge (create) a new log"}
    [:i.fa.fa-magic]
    [:span "Forge"]]
   [:button.ui-btn.ui-btn-dual
    {:tip "Start a system-wide Log Recover process"}
    [:i.fa.fa-search-plus]
    [:span "Recover"]]])

(defn view-header
  [app-id server-cid]
  [:div.a-lv-header.ui-app-header
   [render-flag-area]
   [render-search-area]])

(defn view-body
  [app-id server-cid]
  [:div.a-lv-body [list-entries app-id server-cid]])

(defn view-footer
  [app-id server-cid]
  [:div.a-lv-footer.ui-app-footer
   [render-action-area app-id server-cid]])

(defn ^:export view
  [app-id server-cid]
  [:div.a-lv-container
   [view-header app-id server-cid]
   [view-body app-id server-cid]
   [view-footer app-id server-cid]])
