(ns web.os.popups.os-error.hemacs
  (:require [web.hemacs.utils :as hemacs]))

(defn ^:export mode-info []
  {:id :os-os-error
   :name "BSOD Mode"})

;; walk

(defn walk-ok-btn
  [app-id]
  (println "Walking OK btn")
  (println app-id)
  (let [el-app (hemacs/walk-app app-id)
        el-footer (.querySelector el-app ".os-err-footer")]
    (.querySelector el-footer "button")))

;; ctx

;; input

;; process-input

(defn process-input
  [_ b {app-id :app-id} _]
  (println b)
  (hemacs/dom-click (walk-ok-btn app-id))
  (hemacs/exact-match))
