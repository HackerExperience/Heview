(ns main
  (:require [reagent.core :as reagent]
            [re-frisk.core :refer [enable-re-frisk!]]
            [he.core :as he]
            [he.error]
            [core.view]
            ;; We *have* to require `core.subs` and `core.handlers`, otherwise
            ;; the Google Closure compiler won't load these namespaces.
            ;; That is the only reason that a "dependency" tree must be created
            ;; for them (e.g. `core.subs` requires `game.subs` and `web.subs`).
            [core.subs]
            [core.handlers]

            [taoensso.truss :as truss]))

(defn render
  []
  (reagent/render
   [core.view/view] (js/document.getElementById "app")))

(defn truss-error-handler
  [delayed-map]
  (let [error-data @delayed-map
        error-data (assoc error-data :msg_ @(:msg_ error-data))]
    (he.error/truss-error error-data)
    ;; Even if an error happens, return the wrong value. The user's been
    ;; notified, so we might as well proceed and see what else breaks.
    (:val error-data)))

(defn ^:export init []
  (enable-re-frisk! {:events? false
                     :x -100
                     :y 900})
  (js/console.log "init")
  (he/dispatch-sync [:initialize])
  (truss/set-error-fn! truss-error-handler)
  (render))

;; Reload (do nothing)
(defn reload-noop []
  nil)

;; Reload and redraw all components
(defn reload-redraw []
  ;; NOTE: It might break stuff, e.g. by duplicating `addEventListener`s around
  (reagent/force-update-all))

;; Reload to initial state
(defn reload-scratch []
  (he/dispatch-sync [:driver|sse|close])
  (init))

;; Reload with relogin
(defn reload-relogin []
  (he/dispatch-sync [:driver|sse|close])
  (println "reloading - relogin mode")
  (init)

  (he/dispatch-sync [:web|home|login|form|change-username "asdf"])
  (he/dispatch-sync [:web|home|login|form|change-password "asdfasdf"])
  (he/dispatch-sync [:web|home|login|login])
  )

(defn reload!
  [_]
  ;(reload-noop)
  (reload-redraw)
  ;(reload-scratch)
  ;(reload-relogin)
  )
