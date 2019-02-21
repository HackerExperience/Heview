(ns web.wm.validators
  (:require [taoensso.truss :refer-macros [have?]]
            [he.utils.validators :as utils.v]))

(defn v [fun x]
  (utils.v/v fun x))

(defn- ensure-x-y [x]
  (and (have? [:ks>= #{:x :y}] x)
       (have? pos-int? (:x x))
       (have? pos-int? (:y x))))

(defn drag-click [x]
  (when x
    (ensure-x-y x)))

(defn window-data-config [x]
  (and (have? [:ks>= #{:full-view}] x)
       (have? boolean? (:full-view x))))

(defn window-data [x]
  (and (have? [:ks>= #{:moving? :position :length :z-index :config
                       :focused?}] x)
       (have? boolean? (:moving? x))
       (ensure-x-y (:position x))
       (ensure-x-y (:length x))
       (have? int? (:z-index x))
       (have? boolean? (:focused? x))
       (drag-click (:drag-click x))
       (window-data-config (:config x))))

(defn windows [x]
  (when x
    (doseq [[app-id app-data] x]
      (and (have? string? app-id)
           (window-data app-data)))))

(defn- db-keys [x]
  (have? [:ks>= #{:sessions :next-open :next-z-index :focused-window
                  :window-moving? :active-session :viewport}] x))

(defn db [x]
  (and (db-keys x)
       (have? int? (:next-z-index x))
       (have? [:or nil? string?] (:focused-window x))
       (have? [:or nil? string?] (:window-moving? x))
       (ensure-x-y (:next-open x))
       (ensure-x-y (:viewport x))
       (have? string? (:active-session x))
       (windows (:windows x))))
