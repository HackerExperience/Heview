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
         session-id :session
         context :context
         context-cid :context-cid} (he/subscribe [:web|apps|meta app-id])]
    (if (= app-type :popup)
      (view-popup app-id context-cid popup-info)
      (view-app app-id context-cid app-type))))

(defn full-view-popup
  [app-id server-cid popup-info]
  (let [{popup-app-type :app-type
         popup-type :popup-type
         parent-app-id :parent-id} popup-info]
    [apps.dispatcher/dispatch-popup-full-view
     popup-app-type popup-type app-id server-cid]))

(defn full-view-app
  [app-id server-cid app-type]
  [apps.dispatcher/dispatch-full-view app-type app-id server-cid])

(defn full-view
  [app-id]
  (let [{app-type :type
         popup-info :popup
         server-cid :session} (he/subscribe [:web|apps|meta app-id])]
    (if (= app-type :popup)
      (full-view-popup app-id server-cid popup-info)
      (full-view-app app-id server-cid app-type))))
