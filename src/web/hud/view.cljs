(ns web.hud.view
  (:require [web.hud.conky.view :as hud.conky]
            [web.hud.connection-info.view :as hud.connection-info]
            [web.hud.launcher.view :as hud.launcher]
            ;; [web.hud.system-tray.view :as hud.system-tray]
            [web.hud.taskbar.view :as hud.taskbar]))

(defn view []
  [:div#hud
   [hud.conky/view]
   [hud.connection-info/view]
   [hud.launcher/view]
   [hud.taskbar/view]
   ;; [hud.system-tray/view]
   ])
