(ns web.apps.task-manager.view
  (:require [reagent.core :as reagent]
            [he.core :as he]
            [game.server.process.js :as process.js]))

(def time-left-span-class ".a-tm-b-process-info-eta-span")
(def progress-bar-class ".a-tm-b-process-info-eta-bar-progress")

(defn render-flag-area []
  [:div.a-tm-h-flag-area
   [:div.a-tm-h-flag
    {:tip "Show/hide processes that use CPU"}
    [:i.fas.fa-microchip]]
   [:div.a-tm-h-flag
    {:tip "Show/hide processes that use NET"}
    [:i.fas.fa-exchange-alt]]])

(defn render-search-area []
  [:div.a-tm-h-search-area
   [:div.a-tm-h-search-filter
    {:tip "Advanced filter"}
    [:i.fa.fa-filter]]
   [:div.a-tm-h-search-input
    [:input.ui-input
     {:placeholder "Search"}]
    [:i.fa.fa-search
     {:tip "Search the task manager for a specific term"}]]])

(defn view-header
  [app-id server-cid]
  [:div.a-tm-header
   [render-flag-area]
   [render-search-area]])

(defn render-process-icon
  [{icon-class :icon-class}]
  [:div.a-tm-b-process-icon
   [:i
    {:class icon-class}]])

(defn render-process-desc
  [{action-str :action-str note :tm-note}]
  [:div.a-tm-b-process-desc
   [:span.a-tm-b-process-desc-action action-str]
   [:span.a-tm-b-process-desc-notes note]])

(defn process-info-updater-fn-timer
  [[el-time-left-span _ _] process-id]
  (let [{time-left-str :time-left-str} (process.js/get-timer-var process-id)]
    (set! (.-innerHTML el-time-left-span) time-left-str)))

(defn process-info-updater-fn-progress
  [[_ el-progress-bar el-progress-span] process-id]
  (let [{percentage :percentage-str} (process.js/get-progress-var process-id)]
    (set! (.-innerHTML el-progress-span) percentage)
    (set! (.-width (.-style el-progress-bar)) percentage)))

(defn process-info-updater-interval
  [state]
  (let [proc-id (:process-id @state)
        elements (:elements @state)
        ref-timer (js/setInterval
                   #(process-info-updater-fn-timer elements proc-id) 1000)
        ref-progress (js/setInterval
                   #(process-info-updater-fn-progress elements proc-id) 250)]
    (process-info-updater-fn-timer elements proc-id)
    (process-info-updater-fn-progress elements proc-id)
    [ref-timer ref-progress]))

(defn render-process-info-eta [process-id _tick]
  (let [state (reagent/atom {:process-id process-id
                             :refs nil
                             :elements nil})]
    (reagent/create-class
     {:reagent-render
      (fn []
        [:div.a-tm-b-process-info-eta
         [:div.a-tm-b-process-info-eta-bar
          [:div.a-tm-b-process-info-eta-bar-progress
           [:span]]]
         [:span.a-tm-b-process-info-eta-span]])
      :component-did-mount
      (fn [comp]
        (let [process-id (:process-id @state)
              root (reagent/dom-node comp)
              el-time-left-span (.querySelector root time-left-span-class)
              el-progress-bar (.querySelector root progress-bar-class)
              el-progress-span (.querySelector el-progress-bar "span")
              elements [el-time-left-span el-progress-bar el-progress-span]]
          (process-info-updater-fn-timer elements process-id)
          (process-info-updater-fn-progress elements process-id)
          (swap! state assoc :elements elements)
          (js/setTimeout
           #(swap! state assoc :refs (process-info-updater-interval state))
           (- 1000 (.getMilliseconds (new js/Date))))))
      :component-will-unmount
      (fn [comp]
        (doseq [ref (:refs @state)]
          (js/clearInterval ref)))})))

(defn render-process-info-usage
  [process]
  [:div.a-tm-b-process-info-usage
   [:div.a-tm-b-process-info-usage-entry
    [:i.fas.fa-microchip]
    [:span "33.3%"]]
   [:div.a-tm-b-process-info-usage-entry
    [:i.fas.fa-memory]
    [:span "2.7%"]]])

(defn on-process-click
  [app-id process-id]
  (he/dispatch [:web|apps|task-manager|on-entry-click app-id process-id]))

(defn render-process
  [app-id process-id process]
  (let [client-meta (:client-meta process)]
    [:div.a-tm-b-process-entry
     {:data-process-id process-id}
     [render-process-icon client-meta]
     [render-process-desc client-meta]
     [:div.a-tm-b-process-info
      [render-process-info-eta process-id]
      [render-process-info-usage process]]]))

(defn render-selected-process
  [app-id process-id process]
  [:div.a-tm-b-process-selected
   [render-process app-id process-id process]
   [:div.a-tm-b-process-selected-separator]
   [:div.a-tm-b-process-selected-action-area.ui-btn-area-large
    [:button.ui-btn.ui-btn-icon.a-tm-b-process-selected-action-open
     {:tip "Open process in a popup window"}
     [:i.fas.fa-external-link-alt]]
    [:button.ui-btn.ui-btn-icon.a-tm-b-process-selected-action-pause
     {:tip "Pause the process"}
     [:i.fas.fa-pause]]
    [:button.ui-btn.ui-btn-icon.a-tm-b-process-selected-action-delete
     {:tip "Delete the process"}
     [:i.far.fa-trash-alt]]]])

(defn render-processes-entries
  [app-id processes selected-id]
  [:div.a-tm-b-server-processes
   (for [[process-id process] processes]
     (let [key (str app-id "-" process-id)]
       ^{:key key} [:div.a-tm-b-process-entry-container
                    {:on-click #(on-process-click app-id process-id)}
                    (if (= selected-id process-id)
                      [render-selected-process app-id process-id process]
                      [render-process app-id process-id process])]))])

(defn render-server-processes
  [app-id server-ip server-processes selected-id]
  [:div.a-tm-b-server-entry
   [:div.a-tm-b-server-header
    [:span server-ip]]
   [render-processes-entries app-id server-processes selected-id]])

(defn render-localhost-empty []
  (let [empty-msg "There are no running processes at localhost :("]
    [:div.a-tm-b-server-processes
     [:span.a-tm-b-server-processes-empty empty-msg]]))

(defn render-localhost-processes
  [app-id processes selected-id]
  (let [context (he/subscribe [:web|apps|context app-id])]
    (when-not (= context :remote)
      [:div.a-tm-b-server-entry
       [:div.a-tm-b-server-header
        [:span "localhost"]]
       (if-not (empty? processes)
         [render-processes-entries app-id processes selected-id]
         [render-localhost-empty])])))

(defn view-body
  [app-id server-cid]
  (let [all-processes (he/subscribe [:web|apps|task-manager|entries server-cid])
        selected-id (he/subscribe [:web|apps|task-manager|selected app-id])
        localhost-processes (get all-processes "localhost")]
    [:div.a-tm-body
     ^{:key (str app-id "-localhost")}
     [render-localhost-processes app-id localhost-processes selected-id]
     (for [[ip entries] all-processes]
       (let [key (str app-id "-" ip)]
         (when-not (= "localhost" ip)
           ^{:key key}
           [render-server-processes app-id ip entries selected-id])))]))

(defn view-sidebar
  [app-id server-cid]
  [:div.a-tm-sidebar
   [:div.a-tm-sb-entry
    [:div.a-tm-sb-entry-icon
     [:i.fas.fa-tasks]]
    [:div.a-tm-sb-entry-name
     [:span "Processes"]]]
   [:div.a-tm-sb-entry
    [:div.a-tm-sb-entry-icon
     [:i.fab.fa-freebsd]]
    [:div.a-tm-sb-entry-name
     [:span "Daemons"]]]
   [:div.a-tm-sb-entry
    [:div.a-tm-sb-entry-icon
     [:i.fas.fa-chart-area]]
    [:div.a-tm-sb-entry-name
     [:span "Performance"]]]])

(defn ^:export view
  [app-id server-cid]
  [:div.a-tm-container
   [view-sidebar app-id server-cid]
   [:div.a-tm-main
    [view-header app-id server-cid]
    [view-body app-id server-cid]]])
