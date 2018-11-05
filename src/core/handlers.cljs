(ns core.handlers
  (:require [he.core :as he]
            [core.db]
            [driver.rest.handlers]
            [driver.ws.handlers]
            [game.handlers]
            [web.handlers]))

(he/reg-event-db :initialize
                 (fn [_ _] core.db/initial))
