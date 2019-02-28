(ns web.apps.task-manager.hemacs
  (:require [cljs.core.match :refer-macros [match]]
            [web.hemacs.utils :as hemacs]))

(defn ^:export mode-info []
  {:id :task-manager
   :name "Task Manager Mode"})

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
