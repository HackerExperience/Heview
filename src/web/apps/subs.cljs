(ns web.apps.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]
            [web.apps.software.subs]
            [web.apps.browser.subs]
            [web.apps.chat.subs]
            [web.apps.file-explorer.subs]
            [web.apps.log-viewer.subs]
            [web.apps.remote-access.subs]
            [web.apps.task-manager.subs]))

(defn apps
  [db _]
  (get-in db [:apps]))

(rf/reg-sub
 :web|apps|state
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :state :current])))

(rf/reg-sub
 :web|apps|context
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :meta :context])))

(rf/reg-sub
 :web|apps|context-cid
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :meta :context-cid])))

(rf/reg-sub
 :web|apps|context-nice-name
 (fn [[_ app-id]]
   [(he/subscribed [:web|apps|context app-id])
    (he/subscribed [:web|apps|context-cid app-id])])
 (fn [[context server-cid]]
   (if (= context :local)
     (he/subscribe [:game|server|data|meta|hostname server-cid])
     (:ip (he/subscribe [:game|server|endpoint|link server-cid])))))

(rf/reg-sub
 :web|apps|meta
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :meta])))

(rf/reg-sub
 :web|apps|type
 :<- [:web|apps]
 (fn [db [_ app-id]]
   (get-in db [app-id :meta :type])))


