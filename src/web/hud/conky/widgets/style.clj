(ns web.hud.conky.widgets.style
  (:require [web.hud.conky.widgets.campaign.style :as campaign.style]
            [web.hud.conky.widgets.daemons.style :as daemons.style]
            [web.hud.conky.widgets.multiplayer.style :as multiplayer.style]
            [web.hud.conky.widgets.finances.style :as finances.style]
            [web.hud.conky.widgets.network.style :as network.style]
            [web.hud.conky.widgets.processes.style :as processes.style]
            [web.hud.conky.widgets.resources.style :as resources.style]
            [web.hud.conky.widgets.system.style :as system.style]))

(defn style []
  [[(campaign.style/style)]
   [(daemons.style/style)]
   [(multiplayer.style/style)]
   [(finances.style/style)]
   [(network.style/style)]
   [(processes.style/style)]
   [(resources.style/style)]
   [(system.style/style)]])
