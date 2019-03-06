(ns web.apps.browser.page.not-found.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]))

(defn with-current-tab-custom-callback
  [[_ app-id]]
  [(he/subscribed [:web|apps|browser|tab|custom app-id])])
(def with-current-tab-custom
  #(with-current-tab-custom-callback %))

(rf/reg-sub
 :web|apps|browser|page|not-found|url
 with-current-tab-custom
 (fn [[custom]]
   (:url custom)))
