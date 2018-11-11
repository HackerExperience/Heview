(ns web.subs
  (:require [re-frame.core :as rf]
            [web.home.subs :as home.subs]
            [web.setup.subs :as setup.subs]
            [web.wm.subs :as wm.subs]))

(defn web
  [db _]
  (:web db))

(rf/reg-sub
 :web|home
 home.subs/home)

(rf/reg-sub
 :web|setup
 setup.subs/setup)

(rf/reg-sub
 :web|wm
 :<- [:web]
 wm.subs/wm)

(rf/reg-sub
 :web|state
 (fn [db _]
   (:state db)))

