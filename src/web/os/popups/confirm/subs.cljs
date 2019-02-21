(ns web.os.popups.confirm.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]))

(rf/reg-sub
 :web|os|popups|confirm|config
 he/with-app-state
 (fn [[state] _]
   (:config state)))
