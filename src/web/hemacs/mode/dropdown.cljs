(ns web.hemacs.mode.dropdown
  (:require [clojure.string :as str]
            [cljs.core.match :refer-macros [match]]
            [web.hemacs.utils :as hemacs]))

(def static-enabled-listing
  {"Enter" "Select highlight entry"
   "Escape" "Exit dropdown mode"
   "Home" "Go to first entry"
   "End" "Go to last entry"
   "Page Up" "Move 5 entries up"
   "Page Down" "Move 5 entries down"
   "gg" "Go to first entry"
   "G" "Go to last entry"
   "j" "Highlight next entry"
   "k" "Highlight previous entry"
   "l" "Alias of `Enter`"
   "h" "Alias of `Escape`"
   "i" "Enter search box"
   "s" "Alias of `i`"})

;; walk

(defn walk-container
  [dropdown-id]
  (.getElementById js/document dropdown-id))

(defn walk-selected-container
  [dropdown-id]
  (let [el-dd (walk-container dropdown-id)]
    (.querySelector el-dd ".ui-c-dd-selected-container")))

(defn walk-drop
  [dropdown-id]
  (let [el-dd (walk-container dropdown-id)]
    (.querySelector el-dd ".ui-c-dd-drop")))

(defn walk-drop-search-input
  [dropdown-id]
  (let [el-drop (walk-drop dropdown-id)]
    (.querySelector el-drop ".ui-c-dd-drop-search-input")))

(defn walk-drop-entries
  [dropdown-id]
  (let [el-drop (walk-drop dropdown-id)]
    (.querySelector el-drop ".ui-c-dd-drop-entries")))

(defn walk-drop-entry-selected
  [dropdown-id]
  (let [el-entries (walk-drop-entries dropdown-id)]
    (.querySelector el-entries ".ui-c-dd-drop-entry-selected")))

(defn walk-drop-entry-highlighted
  [dropdown-id]
  (let [el-entries (walk-drop-entries dropdown-id)]
    (.querySelector el-entries ".ui-c-dd-drop-entry-highlighted")))

(defn is-drop-entry-group?
  [el]
  (let [classes (array-seq (.-classList el))]
    (boolean (some #(str/includes? % "-group") classes))))

;; TODO: Several "re-walks" around here...
(defn walk-drop-first-entry
  [el-entries]
  (let [el-empty (.querySelector el-entries ".ui-c-dd-drop-entries-empty")
        first-child (.-firstChild el-entries)]
    (when-not el-empty
      (if (is-drop-entry-group? first-child)
        (.-nextSibling (.-firstChild first-child))
        first-child))))

(defn walk-drop-last-entry
  [el-entries]
  (let [el-empty (.querySelector el-entries ".ui-c-dd-drop-entries-empty")
        last-child (.-lastChild el-entries)]
    (when-not el-empty
      (if (is-drop-entry-group? last-child)
        (.-lastChild last-child)
        last-child))))

(defn keep-walking-next
  [el]
  (let [next-sibling (.-nextSibling el)
        parent-node (.-parentNode el)
        is-parent-group? (is-drop-entry-group? parent-node)]
    (cond
      (and (nil? next-sibling)
           (not is-parent-group?)) nil
      (and (nil? next-sibling)
           is-parent-group?) (let [next-group (.-nextSibling parent-node)]
                               (when-not (nil? next-group)
                                 (.-nextSibling (.-firstChild next-group))))
      :else next-sibling)))

(defn keep-walking-prev
  [el]
  (let [prev-sibling (.-previousSibling el)
        parent-node (.-parentNode el)
        is-parent-group? (is-drop-entry-group? parent-node)]
    (cond
      (and (nil? prev-sibling)
           (not is-parent-group?)) nil
      (and (nil? prev-sibling)
           is-parent-group?) (let [prev-group (.-previousSibling parent-node)]
                               (when-not (nil? prev-group)
                                 (.-lastChild prev-group)))
      (= "SPAN" (.-tagName prev-sibling)) (keep-walking-prev prev-sibling)
      :else prev-sibling)))

(defn walk-drop-next-entry
  [dropdown-id]
  (let [el-entries (walk-drop-entries dropdown-id)
        highlighted-entry (walk-drop-entry-highlighted dropdown-id)]
    (if-not (nil? highlighted-entry)
      (keep-walking-next highlighted-entry)
      (walk-drop-first-entry el-entries))))

(defn walk-drop-prev-entry
  [dropdown-id]
  (let [el-entries (walk-drop-entries dropdown-id)
        highlighted-entry (walk-drop-entry-highlighted dropdown-id)]
    (if-not (nil? highlighted-entry)
      (keep-walking-prev highlighted-entry)
      (walk-drop-first-entry el-entries))))

(defn fn-pgdw-entry
  [el]
  (if-not (nil? el)
    (let [el-next (keep-walking-next el)]
      (if-not (nil? el-next)
        el-next
        el))
    el))

(defn walk-drop-pgdw-entry
  [dropdown-id]
  (let [highlighted-entry (walk-drop-entry-highlighted dropdown-id)
        source-entry (if-not (nil? highlighted-entry)
                       highlighted-entry
                       (walk-drop-first-entry (walk-drop-entries dropdown-id)))]
    (nth (iterate fn-pgdw-entry source-entry) 5)))

(defn fn-pgup-entry
  [el]
  (if-not (nil? el)
    (let [el-prev (keep-walking-prev el)]
      (if-not (nil? el-prev)
        el-prev
        el))
    el))

(defn walk-drop-pgup-entry
  [dropdown-id]
  (let [highlighted-entry (walk-drop-entry-highlighted dropdown-id)
        source-entry (if-not (nil? highlighted-entry)
                       highlighted-entry
                       (walk-drop-first-entry (walk-drop-entries dropdown-id)))]
    (nth (iterate fn-pgup-entry source-entry) 5)))


;; ctx

;; dom

(def highlighted-class
  "ui-c-dd-drop-entry-highlighted")

(defn dom-highlight-entry
  [entry dropdown-id]
  (when-not (nil? entry)
    (let [el-highlighted (walk-drop-entry-highlighted dropdown-id)]
      (when el-highlighted
        (.remove (.-classList el-highlighted) highlighted-class)))
    (.add (.-classList entry) highlighted-class)
    (hemacs/dom-scroll entry)))

;;

(defn highlight-first-entry
  [dropdown-id]
  (let [el-entries (walk-drop-entries dropdown-id)]
    (dom-highlight-entry (walk-drop-first-entry el-entries) dropdown-id)))

(defn highlight-last-entry
  [dropdown-id]
  (let [el-entries (walk-drop-entries dropdown-id)]
    (dom-highlight-entry (walk-drop-last-entry el-entries) dropdown-id)))

(defn highlight-next-entry
  [dropdown-id]
  (dom-highlight-entry (walk-drop-next-entry dropdown-id) dropdown-id))

(defn highlight-prev-entry
  [dropdown-id]
  (dom-highlight-entry (walk-drop-prev-entry dropdown-id) dropdown-id))

;; inputs

(defn input-PageDown
  [[_ _ _ {dropdown-id :dropdown-id}]]
  (dom-highlight-entry (walk-drop-pgdw-entry dropdown-id) dropdown-id)
  (hemacs/exact-match))

(defn input-PageUp
  [[_ _ _ {dropdown-id :dropdown-id}]]
  (dom-highlight-entry (walk-drop-pgup-entry dropdown-id) dropdown-id)
  (hemacs/exact-match))

(defn input-g
  [_]
  (hemacs/multiple-match {"g" "Go to first option"}))

(defn input-gg
  [[_ _ _ {dropdown-id :dropdown-id}]]
  (highlight-first-entry dropdown-id)
  (hemacs/exact-match))

(defn input-G
  [[_ _ _ {dropdown-id :dropdown-id}]]
  (highlight-last-entry dropdown-id)
  (hemacs/exact-match))

(defn input-i
  [[_ _ _ {dropdown-id :dropdown-id}]]
  (let [search-input-el (walk-drop-search-input dropdown-id)]
    (if-not (nil? search-input-el)
      (do
        (hemacs/dom-focus search-input-el)
        (hemacs/exact-match))
      (hemacs/no-match "No search box available"))))

(defn input-j
  [[_ _ _ {dropdown-id :dropdown-id}]]
  (highlight-next-entry dropdown-id)
  (hemacs/exact-match))

(defn input-k
  [[_ _ _ {dropdown-id :dropdown-id}]]
  (highlight-prev-entry dropdown-id)
  (hemacs/exact-match))

(defn input-?
  [[_ _ _ {dropdown-id :dropdown-id}]]
  (let [enabled-keys static-enabled-listing
        disabled-keys {}]
    (hemacs/multiple-match enabled-keys disabled-keys)))

(defn input-Enter
  [[_ _ _ {dropdown-id :dropdown-id}]]
  (let [highlight-entry (walk-drop-entry-highlighted dropdown-id)]
    (if-not (nil? highlight-entry)
      (do
        (hemacs/dom-click highlight-entry)
        (hemacs/exact-match))
      (hemacs/no-match "There are no highlighted entries"))))

(defn input-Escape
  [[_ _ _ {dropdown-id :dropdown-id}]]
  (hemacs/dom-click (walk-selected-container dropdown-id))
  (hemacs/exact-match))

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

(defn alias-s
  [args]
  (input-i args))

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
           ["i"] (input-i args)
           ["j"] (input-j args)
           ["k"] (input-k args)
           ["l"] (alias-l args)
           ["s"] (alias-s args)
           _ (hemacs/no-match))))
