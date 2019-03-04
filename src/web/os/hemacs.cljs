(ns web.os.hemacs
  (:require [cljs.core.match :refer-macros [match]]
            [web.wm.db :as wm.db]
            [web.hemacs.utils :as hemacs]))

;; ctx

;; inputs

(defn input-a [_]
  (hemacs/multiple-match
   {"fe" "file explorer"
    "l" "log viewer"
    "t" "task manager"
    "r" "remote access"}
   {"fw" "firewall"}))

(defn input-af [_]
  (hemacs/multiple-match
   {"e" "file explorer"}
   {"w" "firewall"}))

(defn input-afe [_]
  (hemacs/exact-match [:web|wm|app|open :file-explorer]))

(defn input-al [_]
  (hemacs/exact-match [:web|wm|app|open :log-viewer]))

(defn input-ar [_]
  (hemacs/exact-match [:web|wm|app|open :remote-access]))

(defn input-at [_]
  (hemacs/exact-match [:web|wm|app|open :task-manager]))

(defn winum
  [num gdb {session-id :session-id}]
  (let [wm-db (wm.db/get-context gdb)
        visible-apps (wm.db/get-visible-apps wm-db session-id)
        app-id (nth visible-apps (dec num) nil)]
    (if (nil? app-id)
      (hemacs/no-match (str "There are only " (count visible-apps) " open windows"))
      (hemacs/exact-match [:web|wm|window|focus app-id]))))

(defn input-1
  [[gdb _ ctx _]]
  (winum 1 gdb ctx))

(defn input-2
  [[gdb _ ctx _]]
  (winum 2 gdb ctx))

(defn nomatch [_]
  (hemacs/no-match))

(defn input-spc
  [_]
  (hemacs/multiple-match
   {"a" "applications"
    "0-9" "windows"}))

;; process-input

(defn process-input
  [gdb buffer ctx xargs]
  (let [args [gdb buffer ctx xargs]]
    (match buffer
           ["Space"] (input-spc args)
           ["Space" "a"] (input-a args)
           ["Space" "a" "f"] (input-af args)
           ["Space" "a" "f" "e"] (input-afe args)
           ["Space" "a" "l"] (input-al args)
           ["Space" "a" "r"] (input-ar args)
           ["Space" "a" "t"] (input-at args)
           ["Space" "1"] (input-1 args)
           ["Space" "2"] (input-2 args)
           ;; ["2"] (input-2 args)
           ;; ["3"] (input-3 args)
           ;; ["4"] (input-4 args)
           ;; ["5"] (input-5 args)
           ;; ["6"] (input-6 args)
           ;; ["7"] (input-7 args)
           ;; ["8"] (input-8 args)
           ;; ["9"] (input-9 args)
           else (nomatch else))))
