(ns web.apps.log-viewer.popups.log-edit.hemacs
  (:require [cljs.core.match :refer-macros [match]]
            [web.hemacs.utils :as hemacs]))

(defn ^:export mode-info []
  {:id :log-viewer-log-edit
   :name "Log Edit Mode"})

;; walk

(defn walk-button-edit
  [app-id]
  (let [el-app (hemacs/walk-app app-id)]
    (.querySelector el-app ".a-lv-led-f-button-edit")))

;; ctx

;; inputs

(defn input-e
  [[_ _ {app-id :app-id} _]]
  (hemacs/dom-click (walk-button-edit app-id))
  (hemacs/exact-match))

(defn input-q
  [[_ _ {app-id :app-id} _]]
  (hemacs/he-close-app app-id)
  (hemacs/exact-match))

;; process-input

(defn process-input
  [gdb buffer ctx xargs]
  (let [args [gdb buffer ctx xargs]]
    (match buffer
           ["e"] (input-e args)
           ["q"] (input-q args)
           _ (hemacs/no-match))))
