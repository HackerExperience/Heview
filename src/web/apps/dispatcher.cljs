(ns web.apps.dispatcher
  (:require [he.dispatch]
            [web.apps.log-viewer.db]
            [web.apps.log-viewer.view]))

(defn dispatch-db
  [app-type fun & args]
  (he.dispatch/call (str "web.apps." (name app-type) ".db/" (name fun)) args))

(defn dispatch-view
  [app-type & args]
  (he.dispatch/call (str "web.apps." (name app-type) ".view/view") args))
