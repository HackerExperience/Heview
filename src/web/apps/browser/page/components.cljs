(ns web.apps.browser.page.components
  (:require [he.core :as he]
            [web.ui.components :as ui.components]))

;; Tab ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn page-tab-fn
  [app-id tab-id current-path clicked-path _event]
  (when-not (= current-path clicked-path)
    (he/dispatch [:web|apps|browser|nav|ahref app-id tab-id clicked-path])))

(defn build-tab-data-reducer
  [acc [link-path link-data]]
  (let [entry {:id link-path
               :text (:text link-data)
               :icon (:icon link-data)}]
    (conj acc entry)))

(defn build-tab-data
  [links]
  (reduce build-tab-data-reducer [] links))

(defn tab
  [app-id tab-id links path]
  (let [tab-data (build-tab-data links)
        callback (partial page-tab-fn app-id tab-id path)]
    (ui.components/tab callback tab-data path)))

;; statusbar ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn statusbar-walk
  [app-id]
  (.getElementById js/document (str "a-br-sb-" app-id)))

(defn statusbar-set
  [app-id value]
  (let [element (statusbar-walk app-id)]
    (set! (.-innerHTML element) value)
    (set! (.-display (.-style element)) "block")))

(defn statusbar-unset
  [app-id]
  (let [element (statusbar-walk app-id)]
    (set! (.-innerHTML element) "")
    (set! (.-display (.-style element)) "none")))

;; ahref ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn format-href-id
  [href-id]
  (cond
    (= :index href-id) "/"
    (keyword? href-id) (str "/" (name href-id))
    (= "" href-id) "/"
    (not (= (get href-id 0) "/")) (str "/" href-id)
    :else href-id))

(defn on-link-click
  [app-id tab-id href-id _event]
  (he/dispatch [:web|apps|browser|nav|ahref app-id tab-id href-id])
  (statusbar-unset app-id))

(defn link-fn
  [app-id tab-id href-id]
  #(on-link-click app-id tab-id href-id %))

(defn link
  [app-id tab-id raw-href-id text]
  (let [base-url (he/subscribe [:web|apps|browser|tab|base-url app-id])
        href-id (format-href-id raw-href-id)
        full-url (str base-url href-id)]
    [:span.a-br-pc-a
     {:on-mouse-enter #(statusbar-set app-id full-url)
      :on-mouse-leave #(statusbar-unset app-id)
      :on-click #(on-link-click app-id tab-id href-id %)}
     text]))

;; NotFound (404) ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn not-found
  [app-id tab-id path]
  [:div.a-br-pc-nf
   [:div.a-br-pc-nf-top
    [:h1 "404 Not Found"]]
   [:div.a-br-pc-nf-bottom
    [:span "nginx/1.15.9"]]])
