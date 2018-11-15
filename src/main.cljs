(ns main
  (:require [reagent.core :as reagent]
            [he.core :as he]
            [core.view]
            ;; We *have* to require `core.subs` and `core.handlers`, otherwise
            ;; the Google Closure compiler won't load these namespaces.
            ;; That is the only reason that a "dependency" tree must be created
            ;; for them (e.g. `core.subs` requires `game.subs` and `web.subs`).
            [core.subs]
            [core.handlers]))

(defn render
  []
  (reagent/render [core.view/view] (js/document.getElementById "app")))

(defn ^:export init
  []
  (js/console.log "init")
  (he/dispatch-sync [:initialize])
  (render))

(defn reload!
  []
  (println "reloading") (init))
