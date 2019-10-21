(ns web.hud.conky.widgets.resources.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]))

(rf/reg-sub
 :web|hud|conky|widget|resources|resources
 (fn [_ _]
   ;; Real component data is todo
   [{:type :cpu
     :total "4.0 GHz"
     :used-units "2.0 GHz"
     :used-pct "50"
     :avail "2.0 GHz"}
    {:type :ram
     :total "256 MB"
     :used-units "64 MB"
     :used-pct "25"
     :avail "192 MB"}
    {:type :ram
     :total "256 MB"
     :used-units "64 MB"
     :used-pct "25"
     :avail "192 MB"}
    {:type :hdd
     :total "20 GB"
     :used-units "100 MB"
     :used-pct "0.05"
     :avail "19.9 GB"}]))
