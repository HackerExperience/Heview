(ns web.hud.conky.db)

;; Context ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-context
  [global-db]
  (get-in global-db [:web :hud :conky]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:web :hud :conky] updated-local-db))

;; Model

(def initial-db
  {:widget-area :full
   :widgets-showing [:system :campaign :multiplayer :finances :resources :network :daemons :processes]
   :widgets-db {:system {}
                :campaign {}
                :resources {}
                :network {}
                :daemons {}
                :processes {}}})

;; Bootstrap

(defn bootstrap []
  initial-db)
