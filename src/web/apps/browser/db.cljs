(ns web.apps.browser.db
  (:require [clojure.string :as str]
            [cljs.core.match :refer-macros [match]]
            [web.apps.browser.page.db :as page.db]))

(def open-opts
  {:len-x 600
   :len-y 400
   :config {:icon-class "fab fa-firefox"
            :title "Web Browser"
            :contextable false}})

(defn create-tab-entry
  [tab]
  {:current tab
   :previous []
   :next []})

(defn initial []
  (let [home-tab (page.db/build-tab :home {})]
    {:tabs [(create-tab-entry home-tab)]
     :active-tab-id 0
     :active-tab home-tab}))

(defn get-tabs
  [db]
  (:tabs db))

(defn get-active-tab
  [db]
  (:active-tab db))

(defn get-active-tab-id
  [db]
  (:active-tab-id db))

(defn get-tab-root
  [db tab-id]
  (get-in db [:tabs tab-id]))

(defn get-tab
  [db tab-id]
  (if-not (= tab-id (get-active-tab-id db))
    (:current (get-tab-root db tab-id))
    (get-active-tab db)))

(defn get-tab-input-url
  [db tab-id]
  (:input-url (get-tab db tab-id)))

(defn get-tab-base-url
  [db tab-id]
  (:base-url (get-tab db tab-id)))

(defn- reset-url
  [tab]
  (merge
   tab
   {:input-url (:base-url tab)}))

(defn- set-tab
  [db tab-id tab-root]
  (-> db
      (assoc-in [:tabs tab-id] tab-root)
      (assoc :active-tab-id tab-id)
      (assoc :active-tab (:current tab-root))))

;; Internals

(defn do-close-tab
  [db tab-id]
  (let [old-tabs (get-tabs db)
        next-tab-id (if (= tab-id (dec (count old-tabs)))
                      (dec tab-id)
                      tab-id)
        exists-next-tab? (not (= -1 next-tab-id))
        new-tabs (he.utils/vec-remove old-tabs tab-id)
        next-tab (nth new-tabs next-tab-id {:current nil})]
    (-> db
        (assoc :tabs new-tabs)
        (assoc :active-tab-id next-tab-id)
        (assoc :active-tab (:current next-tab)))))

(defn- do-select-tab
  [db tab-id]
  (-> db
      (assoc :active-tab (get-tab db tab-id))
      (assoc :active-tab-id tab-id)))

(defn- create-new-tab
  [db tab]
  (let [new-tab (create-tab-entry tab)
        old-tabs (get-tabs db)
        new-id (count old-tabs)
        new-tabs (conj old-tabs new-tab)]
    (-> db
        (assoc :tabs new-tabs)
        (do-select-tab new-id))))

(defn- update-tab
  [db tab-id update-fn]
  (let [new-tab (update-fn (get-tab db tab-id))]
    (if (= tab-id (get-active-tab-id db))
      (-> db
          (assoc :active-tab new-tab)
          (assoc-in [:tabs tab-id :current] new-tab))
      (assoc-in db [:tabs tab-id] new-tab))))

;; Navigate

(defn- persist-previous
  "Only adds a tab to `previous` list if it's a new one."
  [previous-tabs old-current new-current]
  (if-not (= (:base-url old-current) (:base-url new-current))
    (conj previous-tabs (reset-url old-current))
    previous-tabs))

(defn- navigate-new
  [db tab-id new-tab]
  (let [{old-current :current
         old-previous :previous
         old-next :next} (get-tab-root db tab-id)
        new-previous (persist-previous old-previous old-current new-tab)
        new-tab-root {:current new-tab
                      :previous new-previous
                      :next old-next}]
    (set-tab db tab-id new-tab-root)))

(defn- do-navigate-back
  [db tab-id]
  (let [{old-current :current
         old-previous :previous
         old-next :next} (get-tab-root db tab-id)
        new-current (last old-previous)
        new-tab-root (fn [] {:current new-current
                             :previous (pop old-previous)
                             :next (into [(reset-url old-current)] old-next)})]
    (if-not (nil? new-current)
      (set-tab db tab-id (new-tab-root))
      db)))

(defn- do-navigate-next
  [db tab-id]
  (let [{old-current :current
         old-previous :previous
         old-next :next} (get-tab-root db tab-id)
        new-current (first old-next)
        new-tab-root {:current new-current
                      :previous (conj old-previous (reset-url old-current))
                      :next (into [] (rest old-next))}]
    (if-not (nil? new-current)
      (set-tab db tab-id new-tab-root)
      db)))

;;

(defn address-handler
  [tab db tab-id]
  (navigate-new db tab-id tab))

;; Event API

(defn navigate-home
  [db tab-id]
  (-> (page.db/build-tab :home {})
      (navigate-new db tab-id)))

(defn navigate-back
  [db tab-id]
  (do-navigate-back db tab-id))

(defn navigate-next
  [db tab-id]
  (do-navigate-next db tab-id))

(defn navigate-href
  [db tab-id path]
  (let [updater-fn (fn [tab]
                     (let [url-suffix (if (= path "/") "" path)
                           new-url (str (:base-url tab) url-suffix)]
                       (-> tab
                           (assoc :path path)
                           (assoc :input-url new-url))))]
    (update-tab db tab-id updater-fn)))

;; TODO: Move to browser-utils or something like
(defn parse-url
  [address]
  (let [[base-url & path-entries] (str/split address #"/")
        path-name (if-not (nil? path-entries)
                   (str "/" (str/join "/" path-entries))
                   "/")]
    {:base-url base-url
     :path-name path-name
     :path-entries path-entries}))

(defn local-path-change
  [tab path db tab-id]
  (let [new-tab (merge tab {:path path})]
    (navigate-new db tab-id new-tab)))

(defn check-path-change
  [db address]
  (let [tab (get-active-tab db)
        {address-base-url :base-url
         path :path-name} (parse-url address)]
    (if (= address-base-url (:base-url tab))
      (partial local-path-change tab path)
      [:backend address-base-url path])))

(defn get-address-handler
  [db address]
  (match address
         "Home" (partial address-handler (page.db/build-tab :home {}))
         _ (check-path-change db address)))

(defn new-tab
  [db]
  (create-new-tab db (page.db/build-tab :home {})))

(defn close-tab
  [db tab-id]
  (do-close-tab db tab-id))

(defn select-tab
  [db tab-id]
  (do-select-tab db tab-id))

(defn update-input-url
  [db tab-id new-url]
  (update-tab db tab-id #(assoc % :input-url new-url)))

(defn browse-ok
  [db tab-id response path]
  (let [tab-type (page.db/keywordize-tab-type (:type response))
        new-tab (page.db/build-tab [tab-type response] {:path path})]
    (navigate-new db tab-id new-tab)))

(defn browse-fail
  [db tab-id status url]
  (let [tab (page.db/build-tab [:not-found url] {})]
    (navigate-new db tab-id tab)))

;; Requests callbacks

(defn callback-browse
  [app-id url tab-id path]
  {:on-ok [:web|apps|browser|browse|ok app-id tab-id url path]
   :on-fail [:web|apps|browser|browse|fail app-id tab-id url path]})

;; WM API

(defn ^:export did-open
  [_ctx app-context _args]
  [:ok (initial) open-opts])
