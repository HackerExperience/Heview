(ns main
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [core.view]
            ;; We *have* to require `core.subs` and `core.events`, otherwise the
            ;; Google Closure compiler won't load these namespaces.
            ;; That is the only reason that a "dependency" tree must be created
            ;; for them (e.g. `core.subs` requires `game.subs` and `os.subs`).
            [core.subs]
            [core.events]))

(defn render
  []
  (reagent/render [core.view/view] (js/document.getElementById "app")))

(defn ^:export init
  []
  (js/console.log "init")
  (rf/dispatch-sync [:initialize])
  (render))

(defn reload! [] (println "reloading") (init))
