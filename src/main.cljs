(ns main
  (:require [reagent.core :as reagent]
            [re-frisk.core :refer [enable-re-frisk!]]
            [he.core :as he]
            [core.view]
            ;; We *have* to require `core.subs` and `core.handlers`, otherwise
            ;; the Google Closure compiler won't load these namespaces.
            ;; That is the only reason that a "dependency" tree must be created
            ;; for them (e.g. `core.subs` requires `game.subs` and `web.subs`).
            [core.subs]
            [core.handlers]

            ;; Temporary requires, until they are required by someone else (?)
            [web.os.popups.db]
            ))

(defn render
  []
  (reagent/render
   [core.view/view] (js/document.getElementById "app"))
  ;(.addEventListener js/window "mouseup" #()) TODO
  )

(defn ^:export init
  []
  (enable-re-frisk!)
  (js/console.log "init")
  (he/dispatch-sync [:initialize])
  (render))

;; Reload (do nothing)
(defn reload-noop []
  nil)

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
  (reload-noop)
  ;(reload-scratch)
  ;(reload-relogin)
  )
