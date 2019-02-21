(ns web.hud.taskbar.view
  (:require [he.core :as he]))

(defn on-taskbar-entry-click
  [app-id]
  (he/dispatch [:web|wm|window|focus app-id]))

(defn render-taskbar-entry
  [{icon-class :icon-class app-name :name app-id :app-id} display-type]
  [:div.hud-tb-entry
   {:on-click #(on-taskbar-entry-click app-id)
    :class (str "hud-tb-" (name display-type) "-entry")}
   [:i {:class icon-class}]
   (when (= display-type :full)
     [:div.hud-tb-full-entry-separator])
   (when (= display-type :full)
     [:span app-name])])

(defn view []
  (let [taskbar-data (he/subscribe [:web|hud|taskbar|entries])]
    [:div#hud-taskbar
     (for [[_ entry] (:entries taskbar-data)]
       ^{:key (str "taskbar-" (:app-id entry))}
       [render-taskbar-entry entry (:display-type taskbar-data)])]))
