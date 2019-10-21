(ns web.hud.style
  (:require [web.hud.conky.style :as conky.style]
            [web.hud.connection-info.style :as connection-info.style]
            [web.hud.launcher.style :as launcher.style]
            [web.hud.system-tray.style :as system-tray.style]
            [web.hud.taskbar.style :as taskbar.style]))

(defn style []
  [[(connection-info.style/style)]
   [(conky.style/style)]
   [(launcher.style/style)]
   [(system-tray.style/style)]
   [(taskbar.style/style)]])
