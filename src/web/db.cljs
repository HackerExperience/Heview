(ns web.db
  (:require [web.home.db :as home.db]))

(def initial
  {:home home.db/initial})

(defn set-meta
  [db meta]
  (assoc-in db [:web :meta :username] meta))

;; (defn post-init
;;   [db cookies]
;;   (if (= :abc cookies)
;;     ))
