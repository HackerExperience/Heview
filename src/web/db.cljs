(ns web.db
  (:require [cljs.core.match :refer-macros [match]]
            [game.db]
            [web.home.db :as home.db]
            [web.wm.db :as wm.db]))

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
