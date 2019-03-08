(ns web.hemacs.dispatcher
  (:require [cljs.core.match :refer-macros [match]]
            [web.os.hemacs :as desktop-mode]
            [web.os.popups.confirm.hemacs :as os-confirm-mode]
            [web.os.popups.os-error.hemacs :as bsod-mode]
            [web.apps.log-viewer.hemacs :as log-viewer-mode]
            [web.apps.log-viewer.popups.log-edit.hemacs :as log-edit-mode]
            [web.apps.task-manager.hemacs :as task-manager-mode]
            [web.hemacs.mode.dropdown :as dropdown-mode]
            [web.hemacs.mode.insert :as insert-mode]))

(def mode-map
  {;; specials
   :insert insert-mode/process-input
   :dropdown dropdown-mode/process-input

   ;; os
   :desktop desktop-mode/process-input
   :os-confirm os-confirm-mode/process-input
   :os-os-error bsod-mode/process-input

   ;; apps
   :log-viewer log-viewer-mode/process-input
   :log-viewer-log-edit log-edit-mode/process-input
   :task-manager task-manager-mode/process-input
   })

(defn- get-software-name
  [app-type]
  (let [[_ & type-suffix] (.split app-type "-")]
    (clojure.string/join "-" type-suffix)))

(defn- get-path-app
  [app-type]
  (let [app-type-str (name app-type)]
    (if (.test #"software-" app-type-str)
      (str "web.apps.software." (get-software-name app-type-str) ".hemacs")
      (str "web.apps." app-type-str ".hemacs"))))

(defn- get-path-popup
  [popup-app popup-type]
  (if (= popup-app :os)
    (str "web.os.popups." (name popup-type) ".hemacs")
    (str "web.apps." (name popup-app) ".popups." (name popup-type) ".hemacs")))

(defn- get-hemacs-path
  [app-path]
  (match app-path
         [:app [app-type]] (get-path-app app-type)
         [:app [popup-app popup-type]] (get-path-popup popup-app popup-type)
         else (he.error/runtime (str "Unknown app-path " app-path))))

(defn dispatch
  [dispatch-path method args]
  (let [path (get-hemacs-path dispatch-path)
        full-path (str path "/" (name method))]
    (he.dispatch/call-me-maybe full-path args)))

(defn dispatch-input
  [id gdb buffer ctx xargs]
  (let [mode-fn (get mode-map id nil)]
    (when (nil? mode-fn)
      (he.error/runtime (str "Unknown mode id " id)))
    (apply mode-fn [gdb buffer ctx xargs])))
