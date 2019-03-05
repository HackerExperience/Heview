(ns web.apps.file-explorer.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]
            [web.apps.file-explorer.db :as file-explorer.db]))

(rf/reg-sub
 :web|apps|file-explorer|storage-id
 he/with-app-state
 (fn [[db]]
   (:selected-storage db)))

(rf/reg-sub
 :web|apps|file-explorer|selected-file
 he/with-app-state
 (fn [[db]]
   (:selected-file db)))

(rf/reg-sub
 :web|apps|file-explorer|view-mode
 he/with-app-state
 (fn [[db]]
   (:view-mode db)))

(rf/reg-sub
 :web|apps|file-explorer|sort-config
 he/with-app-state
 (fn [[db]]
   (:sort-config db)))

(rf/reg-sub
 :web|apps|file-explorer|sort-order
 he/with-app-state
 (fn [[db]]
   (get-in db [:sort-config :sort-order])))

(rf/reg-sub
 :web|apps|file-explorer|filter-config
 he/with-app-state
 (fn [[db]]
   (:filter-config db)))

(rf/reg-sub
 :web|apps|file-explorer|entries
 (fn [[_ app-id server-cid storage-id]]
   [(he/subscribed [:game|server|software|storage|cache server-cid storage-id])
    (he/subscribed [:game|server|software|storage|files server-cid storage-id])
    (he/subscribed [:web|apps|file-explorer|sort-config app-id])
    (he/subscribed [:web|apps|file-explorer|filter-config app-id])])
 (fn [[files-cache source-files sort-config filter-config]]
   ;; The check below is a performance hack. When closing the File Explorer app,
   ;; the underlying state is set to nil, which triggers this subscription to
   ;; rerun, even though the component that uses this subscription (the app)
   ;; will close within milliseconds. Therefore, we check if the returned state
   ;; is empty and if so, ignore the expensive bit of the subscription. Ideally
   ;; the subscription would not be triggered at all, but we do not live in such
   ;; ideal world.
   (when-not (nil? sort-config)
     (file-explorer.db/filter-files
      filter-config sort-config files-cache source-files))))
