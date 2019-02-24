(ns web.apps.log-viewer.popups.log-edit.hemacs
  (:require [cljs.core.match :refer-macros [match]]
            [web.hemacs.utils :as hemacs]))

(defn ^:export mode-info []
  {:id :log-viewer-log-edit
   :name "Log Edit Mode"})

;; walk

;; ctx

;; inputs

(defn input-q
  [[_ _ {app-id :app-id} _]]
  (hemacs/he-close-app app-id)
  (hemacs/exact-match))

;; process-input

(defn process-input
  [gdb buffer ctx xargs]
  (let [args [gdb buffer ctx xargs]]
    (match buffer
           ["q"] (input-q args)
           _ (hemacs/no-match))))
