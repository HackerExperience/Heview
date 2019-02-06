(ns core.handlers
  (:require [he.core :as he]
            [core.db]
            [driver.rest.handlers]
            [driver.sse.handlers]
            [event.handlers]
            [boot.handlers]
            [game.handlers]
            [web.handlers]))

(he/reg-event-fx :initialize
                 (fn [_ _]
                   {:db (core.db/init :web)
                    :dispatch [:core|post-init]}))

(he/reg-event-fx :core|post-init
                 (fn [{:keys [db]} _]
                   (let [event
                         (if (= :web (core.db/get-client db))
                           [:web|post-init]
                           [:mob|post-init])]
                     {:dispatch event})))
