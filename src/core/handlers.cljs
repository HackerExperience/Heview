(ns core.handlers
  (:require [he.core :as he]
            [core.db]
            [home.handlers]
            [setup.handlers]
            [game.handlers]
            [os.handlers]
            [driver.rest.handlers]
            [driver.ws.handlers]))

(he/reg-event-db :initialize
                 (fn [_ _] core.db/initial))
