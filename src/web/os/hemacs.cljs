(ns web.os.hemacs
  (:require [cljs.core.match :refer-macros [match]]
            [web.wm.db :as wm.db]
            [web.hemacs.utils :as hemacs]))

;; ctx

(defn ctx-session
  [gdb]
  (let [wm-db (wm.db/get-context gdb)
        active-session (wm.db/get-active-session wm-db)]
    (wm.db/get-session wm-db active-session)))

;; inputs

(defn input-a [_]
  (hemacs/multiple-match
   {"fe" "file explorer"
    "b" "browser"
    "l" "log viewer"
    "t" "task manager"
    "r" "remote access"}
   {"fw" "firewall"}))

(defn input-ab [_]
  (hemacs/exact-match [:web|wm|app|open :browser]))

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

(defn input-n
  [[gdb _ _ _]]
  (let [session (ctx-session gdb)
        static-enabled-keys {"g" "Gateway notifications"
                             "a" "Account notifications"
                             "c" "Chat notifications"}
        endpoint-key {"e" "Endpoint notifications"}
        enabled-keys (if (nil? (:endpoint session))
                       static-enabled-keys
                       (merge static-enabled-keys endpoint-key))
        disabled-keys (if (nil? (:endpoint session))
                        endpoint-key
                        {})]
    (hemacs/multiple-match
     enabled-keys disabled-keys)))

(defn input-ng [_]
  (-> [:web|hud|connection-info|toggle-notification-panel :gateway]
      (hemacs/exact-match)))

(defn input-ne
  [[gdb _ _ _]]
  (let [session (ctx-session gdb)
        event [:web|hud|connection-info|toggle-notification-panel :endpoint]]
    (if-not (nil? (:endpoint session))
      (hemacs/exact-match event)
      (hemacs/no-match "You are not connected to an endpoint"))))

(defn winum
  [num gdb {session-id :session-id}]
  (let [wm-db (wm.db/get-context gdb)
        visible-apps (wm.db/get-visible-apps wm-db session-id)
        app-id (nth visible-apps (dec num) nil)
        error-msg (str "There are only " (count visible-apps) " open windows")]
    (if (nil? app-id)
      (hemacs/no-match error-msg)
      (hemacs/exact-match [:web|wm|window|focus app-id]))))

(defn input-1
  [[gdb _ ctx _]]
  (winum 1 gdb ctx))

(defn input-2
  [[gdb _ ctx _]]
  (winum 2 gdb ctx))

(defn nomatch [_]
  (hemacs/no-match))

(defn input-Space
  [_]
  (hemacs/multiple-match
   {"a" "applications"
    "n" "notifications"
    "0-9" "windows"}))

;; process-input

(defn process-input
  [gdb buffer ctx xargs]
  (let [args [gdb buffer ctx xargs]]
    (match buffer
           ["Space"] (input-Space args)
           ["Space" "a"] (input-a args)
           ["Space" "a" "b"] (input-ab args)
           ["Space" "a" "f"] (input-af args)
           ["Space" "a" "f" "e"] (input-afe args)
           ["Space" "a" "l"] (input-al args)
           ["Space" "a" "r"] (input-ar args)
           ["Space" "a" "t"] (input-at args)
           ["Space" "n"] (input-n args)
           ["Space" "n" "e"] (input-ne args)
           ["Space" "n" "g"] (input-ng args)
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
