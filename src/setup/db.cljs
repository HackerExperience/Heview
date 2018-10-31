(ns setup.db
  (:require [core.db]))

(def initial
  {:loading? true})

(defn wait-for-servers
  [db total]
  (assoc-in db [:setup :servers-waiting-join] total))

(defn dec-servers-waiting
  [db]
  (update-in db [:setup :servers-waiting-join] dec))

(defn total-servers-waiting
  [db]
  (get-in db [:setup :servers-waiting-join]))

(defn set-boot-failed
  [db]
  (-> db
      (assoc-in [:setup :loading?] false)
      (assoc-in [:setup :boot-failed?] true)))

;; (defn set-state
;;   [db]
;;   (-> db
;;       (core.db/set-state :setup)
;;       (assoc-in db [:home] {})))


