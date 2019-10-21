(ns web.hud.conky.widgets.dispatcher
  (:require [he.dispatch]
            [web.hud.conky.widgets.campaign.view]
            [web.hud.conky.widgets.daemons.view]
            [web.hud.conky.widgets.finances.view]
            [web.hud.conky.widgets.multiplayer.view]
            [web.hud.conky.widgets.network.view]
            [web.hud.conky.widgets.processes.view]
            [web.hud.conky.widgets.resources.view]
            [web.hud.conky.widgets.system.view]))

(defn dispatch
  [widget]
  (let [fun-name (str "web.hud.conky.widgets." (name widget) ".view/view")]
    (he.dispatch/call fun-name [])))
