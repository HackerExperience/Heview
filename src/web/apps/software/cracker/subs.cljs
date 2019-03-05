(ns web.apps.software.cracker.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]))

(defn with-bruteforce-state-callback
  [[_ app-id]]
  [(he/subscribed [:web|apps|software|cracker|bruteforce app-id])])
(def with-bruteforce-state
  #(with-bruteforce-state-callback %))

;; Root

(rf/reg-sub
 :web|apps|software|cracker|tab
 he/with-app-state
 (fn [[db]]
   (:tab db)))

;; Bruteforce

(rf/reg-sub
 :web|apps|software|cracker|bruteforce
 he/with-app-state
 (fn [[db]]
   (:bruteforce db)))

(rf/reg-sub
 :web|apps|software|cracker|bruteforce|ip
 with-bruteforce-state
 (fn [[bruteforce]]
   (:target-ip bruteforce)))
