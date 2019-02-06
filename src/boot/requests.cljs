(ns boot.requests)

(defn on-sync-ok
  [db bootstrap]
  {:dispatch [:boot|sync-flow (:bootstrap bootstrap)]})

(defn on-sync-fail
  [db _]
  {:dispatch [:boot|boot-sync-flow-fail]})

(defn sync
  []
  {:dispatch [:driver|rest|request "POST" "sync" :simple
              {:client "web1"}
              {:on-ok [:boot|req-sync-ok on-sync-ok]
               :on-fail [:boot|req-sync-fail on-sync-fail]}]})


(defn on-subscribe-ok
  [db response]
  {:dispatch [:boot|subscribe-ok]})

(defn on-subscribe-fail
  [db _]
  {:dispatch [:boot|subscribe-fail]})

(defn subscribe
  []
  {:dispatch [:driver|rest|request "POST" "subscribe" :simple
              {}
              {:on-ok [:boot|req-subscribe-ok on-subscribe-ok]
               :on-fail [:boot|req-subscribe-fail on-subscribe-fail]}]})
