(ns home.subs
  (:require [re-frame.core :as rf]
            [home.login.subs]))

(defn home
  [db _]
  (:home db))


(rf/reg-sub
 :home|login
 :<- [:home]
 home.login.subs/login)
