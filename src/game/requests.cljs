(ns game.requests)

(defn on-logout-ok
  [_ _]
  (println "logout ok")
  {:dispatch-n (list [:driver|sse|close] [:initialize])})

(defn on-logout-fail
  [_ response]
  (println (str "logout fail: " response))
  {:dispatch-n (list [:driver|sse|close] [:initialize])})

(defn logout
  []
  {:dispatch [:driver|rest|request "POST" "logout" :simple
              {}
              {:on-ok [:game|req-logout-ok on-logout-ok]
               :on-fail [:game|req-logout-fail on-logout-fail]}]})
