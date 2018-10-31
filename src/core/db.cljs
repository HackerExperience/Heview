(ns core.db
  (:require [cljs.core.match :refer-macros [match]]
            [home.db]
            [game.db]
            [wm.db]))

;; State: home | setup | play
;; OS state exists on Play only 

;; (def initial
;;   {:game game.db/initial
;;    :wm wm.db/initial})


(def initial
  {:state :home
   :home home.db/initial
   })

(defn home->setup
  [db initial]
  (-> db
      (dissoc :home)
      (assoc-in [:state] :setup)
      (assoc-in [:setup] initial)))

(defn setup->play
  [db _]
  (-> db
      (dissoc :setup)
      (assoc-in [:state] :play)))

(defn switch-mode
  [db from to initial]
  (match [from to]
         [:home :setup] (home->setup db initial)
         [:setup :play] (setup->play db initial)
         :else (throw (js/Error. "Wut"))))
