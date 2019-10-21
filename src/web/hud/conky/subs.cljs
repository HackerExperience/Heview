(ns web.hud.conky.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]
            [web.hud.conky.widgets.subs]))

(rf/reg-sub
 :web|hud|conky
 :<- [:web|hud]
 (fn [db _]
   (:conky db)))

(rf/reg-sub
 :web|hud|conky|widget-area
 :<- [:web|hud|conky]
 (fn [db _]
   (:widget-area db)))

(rf/reg-sub
 :web|hud|conky|widgets-showing
 :<- [:web|hud|conky]
 (fn [db _]
   (:widgets-showing db)))

