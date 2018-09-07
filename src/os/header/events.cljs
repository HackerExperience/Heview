(ns os.header.events
  (:require [re-frame.core :as rf]))

(rf/reg-event-db :os|header|inc
                 (fn [db _] (update-in db [:game :server :ip] inc)))
