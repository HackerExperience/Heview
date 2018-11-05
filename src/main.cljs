(ns main
  (:require [reagent.core :as reagent]
            [he.core :as he]
            [core.view]
            ;; We *have* to require `core.subs` and `core.handlers`, otherwise
            ;; the Google Closure compiler won't load these namespaces.
            ;; That is the only reason that a "dependency" tree must be created
            ;; for them (e.g. `core.subs` requires `game.subs` and `os.subs`).
            [core.subs]
            [core.handlers]))

(defn render
  []
  (reagent/render [core.view/view] (js/document.getElementById "app")))

(defn ^:export init
  []
  (js/console.log "init")
  ;; (he/dispatch-sync [:initialize])
  (he/dispatch-sync [:web|setup|boot-flow "SFMyNTY.g3QAAAACZAAEZGF0YW0AAAAkNGE4M2NmYjYtYzNhMi00NDBjLTlmMjctNTE5ODNiNjAxODEwZAAGc2lnbmVkbgYANNCE4GYB.vF19WAQR-2q3C1XxbUm1OUT6jKhd3OrjFG0HsqIlxWM" "d6e2:a8de:618e:c407:16b:e9a:e7f1:3c0a"])
  (render))

(defn reload!
  []
  (println "reloading") (init))
