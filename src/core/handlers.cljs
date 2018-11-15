(ns core.handlers
  (:require [he.core :as he]
            [core.db]
            [driver.rest.handlers]
            [boot.handlers]
            [game.handlers]
            [web.handlers]))

(he/reg-event-db :initialize
                 (fn [_ _]
                   (core.db/init :web)))
