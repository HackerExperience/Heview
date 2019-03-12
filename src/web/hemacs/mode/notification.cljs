(ns web.hemacs.mode.notification
  (:require [cljs.core.match :refer-macros [match]]
            [web.hemacs.utils :as hemacs]))

(def static-enabled-listing
  {"Enter" "Select highlight entry"
   "Escape" "Exit notification mode"
   "Home" "Go to first entry"
   "End" "Go to last entry"
   "Page Up" "Move 5 entries up"
   "Page Down" "Move 5 entries down"
   "gg" "Go to first entry"
   "G" "Go to last entry"
   "j" "Highlight next entry"
   "k" "Highlight previous entry"
   "l" "Alias of `Enter`"
   "h" "Alias of `Escape`"})

;; walk

(defn walk-panel
  [panel-id]
  (.getElementById js/document panel-id))

(defn walk-panel-entries
  [panel-id]
  (let [el-panel (walk-panel panel-id)]
    (.querySelector el-panel ".ui-c-np-entries")))

(defn walk-panel-entry-highlighted
  [panel-id]
  (let [el-entries (walk-panel-entries panel-id)]
    (.querySelector el-entries ".ui-c-np-entry-highlighted")))

(defn walk-panel-first-entry
  [el-entries]
  (let [el-empty (.querySelector el-entries ".ui-c-np-entries-empty")]
    (when-not el-empty
      (.-firstChild el-entries))))

(defn walk-panel-last-entry
  [el-entries]
  (let [el-empty (.querySelector el-entries ".ui-c-np-entries-empty")]
    (when-not el-empty
      (.-lastChild el-entries))))

(defn keep-walking-next
  [el]
  (let [next-sibling (.-nextSibling el)]
    (when-not (nil? next-sibling)
      next-sibling)))

(defn keep-walking-prev
  [el]
  (let [prev-sibling (.-previousSibling el)]
    (when-not (nil? prev-sibling)
      prev-sibling)))

(defn walk-panel-next-entry
  [panel-id]
  (let [el-entries (walk-panel-entries panel-id)
        el-highlighted (walk-panel-entry-highlighted panel-id)]
    (if-not (nil? el-highlighted)
      (keep-walking-next el-highlighted)
      (walk-panel-first-entry el-entries))))

(defn walk-panel-prev-entry
  [panel-id]
  (let [el-entries (walk-panel-entries panel-id)
        el-highlighted (walk-panel-entry-highlighted panel-id)]
    (if-not (nil? el-highlighted)
      (keep-walking-prev el-highlighted)
      (walk-panel-first-entry el-entries))))

(defn fn-pgdw-entry
  [el]
  (if-not (nil? el)
    (let [el-next (keep-walking-next el)]
      (if-not (nil? el-next)
        el-next
        el))
    el))

(defn walk-panel-pgdw-entry
  [panel-id]
  (let [highlighted-entry (walk-panel-entry-highlighted panel-id)
        source-entry (if-not (nil? highlighted-entry)
                       highlighted-entry
                       (walk-panel-first-entry (walk-panel-entries panel-id)))]
    (nth (iterate fn-pgdw-entry source-entry) 5)))

(defn fn-pgup-entry
  [el]
  (if-not (nil? el)
    (let [el-prev (keep-walking-prev el)]
      (if-not (nil? el-prev)
        el-prev
        el))
    el))

(defn walk-panel-pgup-entry
  [panel-id]
  (let [highlighted-entry (walk-panel-entry-highlighted panel-id)
        source-entry (if-not (nil? highlighted-entry)
                       highlighted-entry
                       (walk-panel-first-entry (walk-panel-entries panel-id)))]
    (nth (iterate fn-pgup-entry source-entry) 5)))

;; ctx

;; dom

(def highlighted-class
  "ui-c-np-entry-highlighted")

(defn dom-highlight-entry
  [entry panel-id]
  (when-not (nil? entry)
    (let [el-highlighted (walk-panel-entry-highlighted panel-id)]
      (when el-highlighted
        (.remove (.-classList el-highlighted) highlighted-class)))
    (.add (.-classList entry) highlighted-class)
    (hemacs/dom-scroll entry)))

;; utils

(defn highlight-first-entry
  [panel-id]
  (let [el-entries (walk-panel-entries panel-id)]
    (dom-highlight-entry (walk-panel-first-entry el-entries) panel-id)))

(defn highlight-last-entry
  [panel-id]
  (let [el-entries (walk-panel-entries panel-id)]
    (dom-highlight-entry (walk-panel-last-entry el-entries) panel-id)))

(defn highlight-prev-entry
  [panel-id]
  (dom-highlight-entry (walk-panel-prev-entry panel-id) panel-id))

(defn highlight-next-entry
  [panel-id]
  (dom-highlight-entry (walk-panel-next-entry panel-id) panel-id))

;; input

(defn input-PageDown
  [[_ _ _ {panel-id :panel-id}]]
  (dom-highlight-entry (walk-panel-pgdw-entry panel-id) panel-id)
  (hemacs/exact-match))

(defn input-PageUp
  [[_ _ _ {panel-id :panel-id}]]
  (dom-highlight-entry (walk-panel-pgup-entry panel-id) panel-id)
  (hemacs/exact-match))

(defn input-g
  [_]
  (hemacs/multiple-match {"g" "Go to first entry"}))

(defn input-gg
  [[_ _ _ {panel-id :panel-id}]]
  (highlight-first-entry panel-id)
  (hemacs/exact-match))

(defn input-G
  [[_ _ _ {panel-id :panel-id}]]
  (highlight-last-entry panel-id)
  (hemacs/exact-match))

(defn input-j
  [[_ _ _ {panel-id :panel-id}]]
  (highlight-next-entry panel-id)
  (hemacs/exact-match))

(defn input-k
  [[_ _ _ {panel-id :panel-id}]]
  (highlight-prev-entry panel-id)
  (hemacs/exact-match))

(defn input-Enter
  [[_ _ _ {panel-id :panel-id}]]
  (let [highlighted-entry (walk-panel-entry-highlighted panel-id)]
    (if-not (nil? highlighted-entry)
      (do
        (hemacs/dom-click highlighted-entry)
        (hemacs/exact-match))
      (hemacs/no-match "There are no highlighted entries"))))

(defn input-Escape
  [[_ _ _ {panel-id :panel-id}]]
  (hemacs/dom-click (.querySelector js/document "body"))
  (hemacs/exact-match))

(defn input-?
  [[_ _ _ _]]
  (let [enabled-keys static-enabled-listing
        disabled-keys {}]
    (hemacs/multiple-match enabled-keys disabled-keys)))

;; aliases

(defn alias-ArrowDown
  [args]
  (input-j args))

(defn alias-ArrowUp
  [args]
  (input-k args))

(defn alias-End
  [args]
  (input-G args))

(defn alias-Home
  [args]
  (input-gg args))

(defn alias-h
  [args]
  (input-Escape args))

(defn alias-l
  [args]
  (input-Enter args))

;; process-input

(defn process-input
  [gdb buffer ctx xargs]
  (let [args [gdb buffer ctx xargs]]
    (match buffer
           ["?"] (input-? args)
           ["ArrowDown"] (alias-ArrowDown args)
           ["ArrowUp"] (alias-ArrowUp args)
           ["End"] (alias-End args)
           ["Enter"] (input-Enter args)
           ["Escape"] (input-Escape args)
           ["Home"] (alias-Home args)
           ["PageDown"] (input-PageDown args)
           ["PageUp"] (input-PageUp args)
           ["g"] (input-g args)
           ["g" "g"] (input-gg args)
           ["G"] (input-G args)
           ["h"] (alias-h args)
           ["j"] (input-j args)
           ["k"] (input-k args)
           ["l"] (alias-l args)
           _ (hemacs/no-match))))
