(ns os.header.handlers
  (:require [he.core :as he]))

(he/reg-event-db :os|header|inc
                 (fn [db _]
                   (update-in db [:game :server :ip] inc)))
