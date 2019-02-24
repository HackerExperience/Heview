(ns web.apps.log-viewer.hemacs
  (:require [cljs.core.match :refer-macros [match]]
            [web.hemacs.utils :as hemacs]
            [web.apps.log-viewer.db :as log-viewer.db]
            [web.apps.db :as apps.db]))

(def static-enabled-listing
  {"j" "Select log below"
   "k" "Select log above"
   "gg" "Home"
   "G" "End"})

(def conditional-key-log-selected
  {"e" "Edit selected log"
   "d" "Delete selected log"
   "h" "Hide selected log"
   "l" "Alias of `e`"
   "Enter" "Alias of `e`"})

(defn ^:export mode-info []
  {:id :log-viewer
   :name "Log Viewer Mode"})

;; walk

(defn walk-selected-log
  [app-id]
  (let [el-app (js/document.getElementById app-id)]
    (.querySelector el-app ".lv-selected")))

(defn walk-selected-log-btn-edit
  [app-id]
  (let [el-log (walk-selected-log app-id)]
    (.querySelector el-log ".lv-selected-action-edit")))

(defn walk-first-log
  [app-id]
  (let [el-app (js/document.getElementById app-id)]
    (.querySelector el-app ".lv-entry-container")))

(defn walk-last-log
  [app-id]
  (let [el-app (js/document.getElementById app-id)]
    (last (array-seq (.querySelectorAll el-app ".lv-entry-container")))))

(defn walk-next-log
  [app-id]
  (let [el-selected (walk-selected-log app-id)
        el-parent (.-parentElement el-selected)
        el-next (.-nextSibling el-parent)]
    (if (nil? el-next)
      el-parent
      el-next)))

(defn walk-prev-log
  [app-id]
  (let [el-selected (walk-selected-log app-id)
        el-parent (.-parentElement el-selected)
        el-previous (.-previousSibling el-parent)]
    (if (nil? el-previous)
      el-parent
      el-previous)))

(defn fn-pgdw-log
  [el-parent]
  (let [el-next (.-nextSibling el-parent)]
    (if (nil? el-next)
      el-parent
      el-next)))

(defn walk-pgdw-log
  [app-id]
  (let [el-selected (walk-selected-log app-id)
        el-parent (.-parentElement el-selected)]
    (nth (iterate fn-pgdw-log el-parent) 5)))

(defn fn-pgup-log
  [el-parent]
  (let [el-previous (.-previousSibling el-parent)]
    (if (nil? el-previous)
      el-parent
      el-previous)))

(defn walk-pgup-log
  [app-id]
  (let [el-selected (walk-selected-log app-id)
        el-parent (.-parentElement el-selected)]
    (nth (iterate fn-pgup-log el-parent) 5)))

;; ctx

(defn ctx-app-state
  [gdb app-id]
  (let [app-db (apps.db/get-context gdb)
        app-state (apps.db/get-state app-db app-id)]
    app-state))

(defn ctx-selected-log
  [gdb app-id]
  (let [app-state (ctx-app-state gdb app-id)]
    (log-viewer.db/get-selected-id app-state)))

;; inputs

(defn input-End
  [[_ _ {app-id :app-id} _]]
  (hemacs/dom-click-scroll (walk-last-log app-id))
  (hemacs/exact-match))

(defn input-Home
  [[_ _ {app-id :app-id} _]]
  (hemacs/dom-click-scroll (walk-first-log app-id))
  (hemacs/exact-match))

(defn input-PageDown
  [[gdb _ {app-id :app-id} _]]
  (let [selected-id (ctx-selected-log gdb app-id)]
    (if (nil? selected-id)
      (hemacs/dom-click-scroll (walk-first-log app-id))
      (hemacs/dom-click-scroll (walk-pgdw-log app-id))))
  (hemacs/exact-match))

(defn input-PageUp
  [[gdb _ {app-id :app-id} _]]
  (let [selected-id (ctx-selected-log gdb app-id)]
    (if (nil? selected-id)
      (hemacs/dom-click-scroll (walk-first-log app-id))
      (hemacs/dom-click-scroll (walk-pgup-log app-id) 58)))
  (hemacs/exact-match))

(defn input-?
  [[gdb _ {app-id :app-id} _]]
  (let [selected-id (ctx-selected-log gdb app-id)
        enabled-keys (if (nil? selected-id)
                       static-enabled-listing
                       (merge
                        static-enabled-listing
                        conditional-key-log-selected))
        disabled-keys (if (nil? selected-id)
                        conditional-key-log-selected
                        {})]
    (hemacs/multiple-match enabled-keys disabled-keys)))

(defn input-e
  [[gdb _ {app-id :app-id} _]]
  (let [selected-id (ctx-selected-log gdb app-id)]
    (if-not (nil? selected-id)
      (do
        (hemacs/dom-click (walk-selected-log-btn-edit app-id))
        (hemacs/exact-match))
      (hemacs/no-match "Please select a log first"))))

(defn input-g
  [_]
  (hemacs/multiple-match {"g" "Home"}))

(defn input-j
  [[gdb _ {app-id :app-id} _]]
  (let [selected-id (ctx-selected-log gdb app-id)]
    (if (nil? selected-id)
      (hemacs/dom-click-scroll (walk-first-log app-id))
      (hemacs/dom-click-scroll (walk-next-log app-id))))
  (hemacs/exact-match))

(defn input-k
  [[gdb _ {app-id :app-id} _]]
  (let [selected-id (ctx-selected-log gdb app-id)]
    (if (nil? selected-id)
      (hemacs/dom-click-scroll (walk-first-log app-id))
      (hemacs/dom-click-scroll (walk-prev-log app-id) 58)))
  (hemacs/exact-match))

(defn input-q
  [[_ _ {app-id :app-id} _]]
  (hemacs/he-close-app app-id)
  (hemacs/exact-match))

;; aliases

(defn alias-l
  [args]
  (input-e args))

(defn alias-G
  [args]
  (input-End args))

(defn alias-gg
  [args]
  (input-Home args))

(defn alias-Enter
  [args]
  (input-e args))

;; process-input

(defn process-input
  [gdb buffer ctx xargs]
  (let [args [gdb buffer ctx xargs]]
    (match buffer
           ["End"] (input-End args)
           ["Enter"] (alias-Enter args)
           ["Home"] (input-Home args)
           ["PageDown"] (input-PageDown args)
           ["PageUp"] (input-PageUp args)
           ["?"] (input-? args)
           ["e"] (input-e args)
           ["g"] (input-g args)
           ["g" "g"] (alias-gg args)
           ["G"] (alias-G args)
           ["j"] (input-j args)
           ["k"] (input-k args)
           ["l"] (alias-l args)
           ["q"] (input-q args)
           _ (hemacs/no-match))))
