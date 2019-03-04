(ns web.hud.system-tray.view
  (:require [reagent.core :as reagent]
            [he.core :as he]
            [he.date]))

(defn render-daemon-area []
  [:div.hud-st-daemon-area
   [:div.hud-st-daemon-entry
    [:i.fab.fa-freebsd]]
   [:div.hud-st-daemon-entry
    [:i.fab.fa-dev]]
   [:div.hud-st-daemon-entry
    [:i.fas.fa-wifi]]])

(defn format-clock-day
  [day month]
  (str day "/" month))

(defn format-clock-time
  [hour minute]
  (str hour ":" minute))

(defn clock-set-day
  [element]
  (let [date (new js/Date)
        day (he.date/format-day-numeric date)
        month (he.date/format-month-numeric date)]
    (set! (.-innerHTML element) (format-clock-day day month))))

(defn clock-set-time
  [element]
  (let [date (new js/Date)
        hour (he.date/format-hour-numeric date)
        minute (he.date/format-minute-numeric date)]
    (set! (.-innerHTML element) (format-clock-time hour minute))))

(defn create-clock-interval
  [el-day el-time]
  (clock-set-day el-day)
  (clock-set-time el-time)
  (js/setInterval #(clock-set-day el-day) 60000)
  (js/setInterval #(clock-set-time el-time) 60000))

(defn render-clock []
  (reagent/create-class
   {:reagent-render
    (fn []
      [:div.hud-st-clock-area
       [:span.hud-st-clock-day]
       [:span.hud-st-clock-time]])
    :component-did-mount
    (fn [comp]
      (let [root (reagent/dom-node comp)
            el-clock-day (.querySelector root ".hud-st-clock-day")
            el-clock-time (.querySelector root ".hud-st-clock-time")
            now (new js/Date)
            wait (-> now
                     (.getSeconds)
                     (* 1000)
                     (->> (- 60000))
                     (- (.getMilliseconds now))
                     (+ 15))]
        (js/setTimeout #(create-clock-interval el-clock-day el-clock-time) wait)
        (clock-set-day el-clock-day)
        (clock-set-time el-clock-time)))}))

(defn view []
  [:div#hud-system-tray
   [render-daemon-area]
   [:div.hud-st-clock-separator]
   [render-clock]])
