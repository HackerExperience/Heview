(ns web.apps.browser.handlers
  (:require [he.core :as he]
            [web.apps.db :as apps.db]
            [web.wm.db :as wm.db]
            [web.apps.browser.page.handlers]
            [web.apps.browser.db :as browser.db]))

(he/reg-event-db
 :web|apps|browser|tab|new
 (fn [gdb [_ app-id]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db ldb app-id #(browser.db/new-tab %) nil)
     (apps.db/set-context gdb ldb))))

(he/reg-event-fx
 :web|apps|browser|tab|close
 (fn [{gdb :db} [_ app-id tab-id]]
   (let [apps-db (apps.db/get-context gdb)
         new-apps-db (apps.db/update-db
                      apps-db app-id #(browser.db/close-tab % tab-id) nil)
         all-closed? (apps.db/with-app-state
                       new-apps-db app-id #(nil? (:active-tab %)))]
     (if-not all-closed?
       {:db (apps.db/set-context gdb new-apps-db)}
       {:dispatch [:web|wm|app|close app-id]}))))

(he/reg-event-db
 :web|apps|browser|tab|select
 (fn [gdb [_ app-id tab-id]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db ldb app-id #(browser.db/select-tab % tab-id) nil)
     (apps.db/set-context gdb ldb))))

(he/reg-event-db
 :web|apps|browser|tab|input-url
 (fn [gdb [_ app-id tab-id value]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db
      ldb app-id #(browser.db/update-input-url % tab-id value) nil)
     (apps.db/set-context gdb ldb))))

;; Navigate buttons

(he/reg-event-db
 :web|apps|browser|nav|home
 (fn [gdb [_ app-id tab-id]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db ldb app-id #(browser.db/navigate-home % tab-id) nil)
     (apps.db/set-context gdb ldb))))

(he/reg-event-db
 :web|apps|browser|nav|back
 (fn [gdb [_ app-id tab-id]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db ldb app-id #(browser.db/navigate-back % tab-id) nil)
     (apps.db/set-context gdb ldb))))

(he/reg-event-db
 :web|apps|browser|nav|next
 (fn [gdb [_ app-id tab-id]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db ldb app-id #(browser.db/navigate-next % tab-id) nil)
     (apps.db/set-context gdb ldb))))

;; Ahref

(he/reg-event-db
 :web|apps|browser|nav|ahref
 (fn [gdb [_ app-id tab-id new-path]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db
      ldb app-id #(browser.db/navigate-href % tab-id new-path) nil)
     (apps.db/set-context gdb ldb))))

;; Browse request

(defn- dispatch-handler
  [gdb app-id tab-id handler-fn]
  {:db (as-> (apps.db/get-context gdb) ldb
         (apps.db/update-db ldb app-id #(handler-fn % tab-id) nil)
         (apps.db/set-context gdb ldb))})

(defn- dispatch-browse
  [gdb app-id tab-id [_ base-url path]]
  (let [wm-db (wm.db/get-context gdb)
        server-cid (wm.db/get-active-session wm-db)
        callback (browser.db/callback-browse app-id base-url tab-id path)]
    {:dispatch [:game|network|browse server-cid base-url callback]}))

(defn- handle-browse
  [gdb app-id tab-id url]
  (let [apps-db (apps.db/get-context gdb)
        browser-db (apps.db/get-state apps-db app-id)
        handler (browser.db/get-address-handler browser-db url)]
    (if (fn? handler)
      (dispatch-handler gdb app-id tab-id handler)
      (dispatch-browse gdb app-id tab-id handler))))

(he/reg-event-fx
 :web|apps|browser|browse-url
 (fn [{gdb :db} [_ app-id tab-id url]]
   (handle-browse gdb app-id tab-id url)))

(he/reg-event-fx
 :web|apps|browser|browse
 (fn [{gdb :db} [_ app-id tab-id]]
   (let [apps-db (apps.db/get-context gdb)
         browser-db (apps.db/get-state apps-db app-id)
         url (browser.db/get-tab-input-url browser-db tab-id)]
     (handle-browse gdb app-id tab-id url))))

(he/reg-event-db
 :web|apps|browser|browse|ok
 (fn [gdb [_ status response g [app-id tab-id url path]]]
   (as-> (apps.db/get-context gdb) ldb
     (apps.db/update-db
      ldb app-id #(browser.db/browse-ok % tab-id response path) nil)
     (apps.db/set-context gdb ldb))))

(he/reg-event-fx
 :web|apps|browser|browse|fail
 (fn [{gdb :db} [_ status r g [app-id tab-id url]]]
   (let [apps-db (apps.db/get-context gdb)
         new-apps-db (apps.db/update-db
                      apps-db app-id
                      #(browser.db/browse-fail % tab-id status url) nil)
         dispatch (when-not (= status 404)
                    (let [config
                          (he.error/generic-response-config status nil app-id)]
                      [:web|wm|perform [:confirm app-id config]]))]
     {:db (apps.db/set-context gdb new-apps-db)
      :dispatch-n (list dispatch)})))
