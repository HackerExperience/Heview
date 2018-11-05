(ns web.home.subs
  (:require [re-frame.core :as rf]
            [web.home.login.subs :as login.subs]))

(defn home
  [db _]
  (:home db))


(rf/reg-sub
 :home|login
 :<- [:home]
 login.subs/login)
