(ns core.db
  (:require [cljs.core.match :refer-macros [match]]
            [he.utils]
            [web.db]))

(defn get-client
  [db]
  (get-in db [:core :client]))

(defn get-client-init
  [client]
  (if (= client :web)
    web.db/initial
    {:mobile :todo}))

(defn init
  [client]
  (merge
   {:core {:client client :state :home}}
   (hash-map client (get-client-init client))))

;; Mode transitions ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn home->boot
  [db initial client]
  (-> db
      (he.utils/dissoc-in [:home])
      (assoc-in [:core :state] :boot)
      (assoc-in [:boot] initial)))

(defn boot->play
  [db _ client]
  (-> db
      (he.utils/dissoc-in [:boot])
      (assoc-in [:core :state] :play)))

(defn switch-mode
  [db from to initial]
  (let [client (get-client db)]
    (match [from to]
           [:home :boot] (home->boot db initial client)
           [:boot :play] (boot->play db initial client)
           :else (throw (js/Error. "Wut")))))
