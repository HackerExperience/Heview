(ns web.apps.view
  (:require [he.core :as he]
            [web.apps.dispatcher :as apps.dispatcher]))

(defn view-popup
  [app-id server-cid popup-info]
  (let [{popup-app-type :app-type
         popup-type :popup-type
         parent-app-id :parent-id} popup-info]
    [apps.dispatcher/dispatch-popup-view
     popup-app-type popup-type app-id server-cid]))

(defn view-app
  [app-id server-cid app-type]
  [apps.dispatcher/dispatch-view app-type app-id server-cid])

(defn view
  [app-id]
  (let [{app-type :type
         popup-info :popup
         session-id :session} (he/subscribe [:web|apps|meta app-id])
        server-cid (keyword session-id)]
    (if (= app-type :popup)
      (view-popup app-id server-cid popup-info)
      (view-app app-id server-cid app-type))))

