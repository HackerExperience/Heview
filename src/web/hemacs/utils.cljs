(ns web.hemacs.utils)

;; Result handling

(defn no-match
  ([]
   [:no-match])
  ([reason]
   [:no-match reason]))

(defn exact-match
  ([]
   [:exact-match])
  ([actions]
   (let [list-actions (if (vector? (first actions))
                        actions
                        [actions])]
     [:exact-match list-actions])))

(defn multiple-match
  ([enabled-map]
   (multiple-match enabled-map {}))
  ([enabled-map disabled-map]
   [:multiple-match {:enabled enabled-map
                     :disabled disabled-map}]))


;; Walk utils

(defn walk-app
  [app-id]
  (.getElementById js/document app-id))

(defn walk-app-header
  [app-id]
  (let [el-app (walk-app app-id)]
    (.querySelector el-app ".app-header")))

(defn upwalk-scroller
  [element]
  (if (> (.-scrollHeight element) (.-clientHeight element))
    element
    (upwalk-scroller (.-parentElement element))))

(defn walk-marker
  [elements marker-id]
  (nth elements (dec marker-id) nil))

;; Dom utils

(defn dom-unpaint-markers
  []
  (let [markers (array-seq (.querySelectorAll js/document ".hemacs-marker"))]
    (doseq [el-marker markers]
      (.remove el-marker))))

(defn dom-paint-markers
  [elements]
  (doseq [[i element] (map-indexed vector elements)]
    (let [html-marker (str "<div class=\"hemacs-marker\"><span>"
                           (inc i)
                           "</span></div>")]
      (.insertAdjacentHTML element "beforeBegin" html-marker))))

(defn dom-focus
  [element]
  (when-not (nil? element)
    (.focus element)
    element))

(defn dom-click
  [element]
  (when-not (nil? element)
    (.click element)
    element))

(defn dom-scroll
  ([element]
   (dom-scroll element 0))
  ([element custom-offset]
   (let [scroller (upwalk-scroller element)
         base-height (int (* 0.75 (.-clientHeight scroller)))
         new-height (- (.-offsetTop element) (- base-height custom-offset))]
     (set! (.-scrollTop scroller) new-height))
   element))

(defn dom-click-scroll
  ([element]
   (dom-click-scroll element 0))
  ([element custom-offset]
   (dom-click element)
   (dom-scroll element custom-offset)
   element))

;; HE utils

(defn he-close-app
  [app-id]
  (let [el-header (walk-app-header app-id)]
    (dom-click (.querySelector el-header ".fa-window-close"))))
