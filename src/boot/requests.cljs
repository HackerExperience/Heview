(ns boot.requests
  (:require [driver.rest.request :as request]))

(defn on-sync-ok
  [db [_ bootstrap]]
  (println "Sync ok")
  {:dispatch [:boot|sync-flow (:bootstrap bootstrap)]})

(defn on-sync-fail
  [db _]
  {:dispatch [:boot|boot-sync-flow-fail]})

(defn sync []
  (request/sync {:on-ok [:boot|req-sync-ok on-sync-ok]
                 :on-fail [:boot|req-sync-fail on-sync-fail]}))


(defn on-subscribe-ok
  [db [_ response]]
  {:dispatch [:boot|subscribe-ok]})

(defn on-subscribe-fail
  [db _]
  {:dispatch [:boot|subscribe-fail]})

(defn subscribe []
  (request/subscribe {:on-ok [:boot|req-subscribe-ok on-subscribe-ok]
                      :on-fail [:boot|req-subscribe-fail on-subscribe-fail]}))
