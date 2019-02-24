(ns web.hemacs.mode.insert
  (:require [cljs.core.match :refer-macros [match]]
            [web.hemacs.utils :as hemacs]))

(defn input-Escape []
  (.blur (.-activeElement js/document))
  (hemacs/no-match))

(defn process-input
  [gdb buffer ctx xargs]
  (match buffer
         ["Escape"] (input-Escape)
         _ (hemacs/no-match)))
