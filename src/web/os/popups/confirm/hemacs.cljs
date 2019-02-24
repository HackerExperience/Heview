(ns web.os.popups.confirm.hemacs
  (:require [cljs.core.match :refer-macros [match]]
            [web.hemacs.utils :as hemacs]))

(defn ^:export mode-info []
  {:id :os-confirm
   :name "OS Confirm Mode"})

;; walk

(defn walk-button-area
  [app-id]
  (let [el-app (hemacs/walk-app app-id)]
    (.querySelector el-app ".os-cfm-button-area")))

(defn walk-buttons
  [app-id]
  (let [el-button-area (walk-button-area app-id)]
    (array-seq (.querySelectorAll el-button-area "button"))))

;; ctx

;; (defn ctx-num-buttons
;;   [app-id]
;;   (count (array-seq (.querySelectorAll (walk-button-area app-id) "button"))))

;; input

(defn- reducer-input-b
  [acc x]
  (merge acc
         (hash-map (str (inc x)) (str "Click button #" (inc x)))))

(defn input-b
  [[_ _ {app-id :app-id} _]]
  (let [buttons (walk-buttons app-id)
        enabled-keys (reduce reducer-input-b {} (range (count buttons)))]
    (hemacs/dom-paint-markers buttons)
    [:multiple-match
     {:enabled enabled-keys :disabled {}}
     {:with-markers? true}]))

(defn input-b_
  [[_ [_ marker-id] {app-id :app-id} _]]
  (let [buttons (walk-buttons app-id)
        el-marker (hemacs/walk-marker buttons marker-id)]
    (if-not (nil? el-marker)
      (do
        (hemacs/dom-click el-marker)
        (hemacs/exact-match))
      (hemacs/no-match))))

;; process-input

(defn process-input
  [gdb buffer ctx xargs]
  (let [args [gdb buffer ctx xargs]]
    (match buffer
           ["b"] (input-b args)
           ["b" _] (input-b_ args)
           _ (hemacs/no-match))))
