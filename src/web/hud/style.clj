(ns web.hud.style
  (:require [web.hud.connection-info.style :as connection-info.style]
            [web.hud.launcher.style :as launcher.style]
            [web.hud.system-tray.style :as system-tray.style]
            [web.hud.taskbar.style :as taskbar.style]))

(defn local-style []
  [])

(defn global-style []
  [[:#hud
    {}
    [(connection-info.style/local-style)]
    [(launcher.style/local-style)]
    [(system-tray.style/local-style)]
    [(taskbar.style/local-style)]]])
