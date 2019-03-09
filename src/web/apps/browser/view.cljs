(ns web.apps.browser.view
  (:require [cljs.core.match :refer-macros [match]]
            [he.core :as he]
            [web.apps.browser.page.view :as page.view]))

(defn on-nav-bar-change
  [app-id tab-id event]
  (he/dispatch
   [:web|apps|browser|tab|input-url app-id tab-id (-> event .-target .-value)]))

(defn- on-nav-bar-keyup
  [app-id tab-id event]
  (when (= (.-key event) "Enter")
    (he/dispatch [:web|apps|browser|browse app-id tab-id])))

(defn- on-header-click
  [app-id tab-id action-id _event]
  (let [event
        (match action-id
               :home [:web|apps|browser|nav|home app-id tab-id]
               :back [:web|apps|browser|nav|back app-id tab-id]
               :next [:web|apps|browser|nav|next app-id tab-id]
               else (he.error/match "Invalid header action" else))]
    (when (vector? event)
      (he/dispatch event))))

(defn render-header
  [app-id]
  (let [tab-id (he/subscribe [:web|apps|browser|tab|id app-id])
        url (he/subscribe [:web|apps|browser|tab|input-url app-id])
        previous? (he/subscribe [:web|apps|browser|tab|previous? app-id])
        next? (he/subscribe [:web|apps|browser|tab|next? app-id])]
    [:div.a-br-header
     [:div.a-br-h-nav-buttons
      [:div.a-br-h-nav-button.a-br-h-nav-button-caret
       {:on-click #(on-header-click app-id tab-id :back %)}
       [:i.fa.fa-caret-left
        {:class (when-not previous?
                  :br-h-nav-button-disabled)}]]
      [:div.a-br-h-nav-button.a-br-h-nav-button-caret
       {:on-click #(on-header-click app-id tab-id :next %)}
       [:i.fa.fa-caret-right
        {:class (when-not next?
                  :br-h-nav-button-disabled)}]]
      [:div.a-br-h-nav-button.a-br-h-nav-button-reload
       [:i.fas.fa-sync-alt]]
      [:div.a-br-h-nav-button
       {:on-click #(on-header-click app-id tab-id :home %)}
       [:i.fas.fa-home]]]

     [:div.a-br-h-nav-bar
      [:input.a-br-h-nav-bar-input.ui-input
       {:value url
        :on-change #(on-nav-bar-change app-id tab-id %)
        :on-key-up #(on-nav-bar-keyup app-id tab-id %)}]]]))


(defn on-tab-click
  [app-id tab-id _event]
  (he/dispatch [:web|apps|browser|tab|select app-id tab-id ]))

(defn on-new-tab-click
  [app-id _event]
  (he/dispatch [:web|apps|browser|tab|new app-id]))

(defn on-tab-close-click
  [app-id tab-id event]
  (he/dispatch [:web|apps|browser|tab|close app-id tab-id])
  (.stopPropagation event))

(defn render-sidebar-tab
  [app-id [tab-id tab] active?]
  [:div.a-br-s-tab
   {:class (when active?
             :br-s-tab-active)
    :on-click #(on-tab-click app-id tab-id %)}
   [:div.a-br-s-tab-icon
    [:i {:class (:favicon tab)}]]
   [:div.a-br-s-tab-title
    [:span (:title tab)]]
   [:div.a-br-s-tab-close
    {:on-click #(on-tab-close-click app-id tab-id %)}
    [:i.fas.fa-times]]])

(defn render-sidebar
  [app-id]
  (let [tabs (he/subscribe [:web|apps|browser|tabs app-id])
        tab-id (he/subscribe [:web|apps|browser|tab|id app-id])]
    [:div.a-br-sidebar
     [:div.a-br-s-tabs
      (for [[i {tab :current}] (map-indexed vector tabs)]
        (let [key (str app-id "-s-tab-" i)
              active? (= tab-id i)]
          ^{:key key} [render-sidebar-tab app-id [i tab] active?]))]
     [:div.a-br-s-new-tab
      [:button.ui-btn.a-br-s-new-tab-button
       {:tip "Open new tab"
        :on-click #(on-new-tab-click app-id %)}
       [:i.far.fa-plus-square]]]]))

(defn render-status-bar
  [app-id]
  [:div.a-br-v-sb
   {:id (str "a-br-sb-" app-id)}
   [:span ""]])

(defn render-viewport
  [app-id]
  [:div.a-br-viewport
   (let [page (he/subscribe [:web|apps|browser|tab|page app-id])
         tab-id (he/subscribe [:web|apps|browser|tab|id app-id])
         links (he/subscribe [:web|apps|browser|tab|links app-id])
         path (he/subscribe [:web|apps|browser|tab|path app-id])]
     (page.view/render-page app-id tab-id [links path] page))
   [render-status-bar app-id]])

(defn render-body
  [app-id]
  [:div.a-br-body
   [render-sidebar app-id]
   [render-viewport app-id]])

(defn ^:export view
  [app-id server-cid]
  [:div.a-br-container
   [render-header app-id]
   [render-body app-id]])
