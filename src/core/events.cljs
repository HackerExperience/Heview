(ns core.events
  (:require [re-frame.core :as rf]
            [core.db]
            [os.events]))

(rf/reg-event-db :initialize (fn [_ _] core.db/initial))
