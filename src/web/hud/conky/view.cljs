(ns web.hud.conky.view
  (:require [reagent.core :as r]
            [he.core :as he]
            [he.date]
            [web.hud.conky.ui :as ckui]
            [web.hud.conky.widgets.dispatcher :as widgets.dispatcher]))

(defn render-header-info []
  [:div.hud-ck-h-p-info.hud-ckui-subitem
   [:div.hud-ck-h-p-info-entry
    {:tip "Global ranking"}
    [:i.fa.fa-globe-americas.hud-ck-h-p-info-entry-icon]
    [:span "309"]]
   [:div.hud-ck-h-p-info-entry
    {:tip "National ranking"}
    [:i.fas.fa-flag.hud-ck-h-p-info-entry-icon]
    [:span "24"]]])

(defn render-header-actions []
  [:div.hud-ck-h-actions
   [:div.hud-ck-h-a-action
    {:tip "Account notifications"}
    [:i.fas.fa-user]]
   [:div.hud-ck-h-a-action
    [:i.far.fa-comments]]
   [:div.hud-ck-h-a-action
    [:i.far.fa-building]]
   [:div.hud-ck-h-a-action
    [:i.fas.fa-user-cog]]
   [:div.hud-ck-h-a-action
    {:tip "Logout"}
    [:i.fas.fa-power-off]]])

(defn view-header []
  (let [wa-size (he/subscribe [:web|hud|conky|widget-area])]
    [:<>
     [:div.hud-ck-header
      [:div.hud-ck-h-player
       [:div.hud-ck-h-p-avatar]
       [:div.hud-ck-h-p-name
        "napsterbr"]
       [render-header-info]]
      [render-header-actions]]
     (when (= wa-size :small)
       [:div.hud-ck-header-separator.hud-ck-header-separator-external])]))

(defn view-widget [widget]
  [:<>
   [:div.hud-ck-wa-widget
    [:div.hud-ck-wa-w-header
     [:span (str widget)]]
    [:div.hud-ck-wa-w-body
     [widgets.dispatcher/dispatch widget]]]])

(defn view-widget-area []
  (let [wa-size (he/subscribe [:web|hud|conky|widget-area])
        wa-class (if (= wa-size :full)
                   :hud-ck-widget-area-full
                   :hud-ck-widget-area-small)
        widgets-showing (he/subscribe [:web|hud|conky|widgets-showing])]
    [:div.hud-ck-widget-area
     {:class wa-class}
     (for [widget widgets-showing]
       ^{:key widget} [view-widget widget])]))

(defn format-clock-date
  [day month]
  (str month ", " day))

(defn format-clock-time
  [hour minute]
  (str hour ":" minute))

(defn clock-set-date
  [element]
  (let [date (new js/Date)
        day (he.date/format-day-numeric date)
        month (he.date/format-month-word date)]
    (set! (.-innerHTML element) (format-clock-date day month))))

(defn clock-set-time
  [element]
  (let [date (new js/Date)
        hour (he.date/format-hour-numeric date)
        minute (he.date/format-minute-numeric date)]
    (set! (.-innerHTML element) (format-clock-time hour minute))))

(defn create-clock-interval
  [el-date el-time]
  (clock-set-date el-date)
  (clock-set-time el-time)
  (js/setInterval #(clock-set-date el-date) 60000)
  (js/setInterval #(clock-set-time el-time) 60000))

(defn render-clock []
  (r/create-class
   {:reagent-render
    (fn []
      [:div.hud-ck-f-main
       [:div.hud-ck-f-m-time
        [:span]]
       [:div.hud-ck-f-m-date
        [:span]]])
    :component-did-mount
    (fn [comp]
      (let [root (r/dom-node comp)
            el-clock-time (.querySelector root ".hud-ck-f-m-time")
            el-clock-date (.querySelector root ".hud-ck-f-m-date")
            wait (he.date/milliseconds-till-next-minute)]
        (js/setTimeout
         #(create-clock-interval el-clock-date el-clock-time) wait)
        (clock-set-date el-clock-date)
        (clock-set-time el-clock-time)))}))

(defn view-footer []
  (let [wa-size (he/subscribe [:web|hud|conky|widget-area])]
    [:<>
     [:div.hud-ck-footer
      [render-clock]
      [:div.hud-ck-f-footer
       [:div.hud-ck-f-f-launcher
        [:div.hud-ck-f-f-launcher-entry
         [:i.fas.fa-wifi]]
        [:div.hud-ck-f-f-launcher-entry
         [:i.fas.fa-wrench]]
        [:div.hud-ck-f-f-launcher-entry
         [:i.far.fa-calendar-alt]]
        [:div.hud-ck-f-f-launcher-entry
         [:i.fas.fa-info]]]
       [:div.hud-ck-f-f-version
        [:span "v2019.03.26.01"]]]]
     (if (= wa-size :full)
       [:div.hud-ck-footer-separator.hud-ck-footer-separator-internal]
       [:div.hud-ck-footer-separator.hud-ck-footer-separator-external])]))

(defn ^:export view []
  [:div#hud-conky
   [view-header]
   [view-widget-area]
   [view-footer]])
