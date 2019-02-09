(ns web.apps.dispatcher
  (:require [he.dispatch]
            [web.apps.log-viewer.db]
            [web.apps.log-viewer.view]
            [web.apps.log-viewer.popups.log-edit.db]))

(defn- get-app-prefix
  [app-type]
  (if (= app-type :os)
    "web.os.popups"
    (str "web.apps." (name app-type))))

(defn dispatch-db
  [app-type fun & args]
  (he.dispatch/call (str (get-app-prefix app-type) ".db/" (name fun))
                    ;(first (apply conj args))
                    (apply conj args)

                    ))

(defn dispatch-view
  [app-type & args]
  (he.dispatch/call (str "web.apps." (name app-type) ".view/view") args))

(defn dispatch-full-view
  [app-type & args]
  (he.dispatch/call (str "web.apps." (name app-type) ".view/full-view") args))

;; Popups

(defn get-popup-prefix
  [app-type]
  (if (= app-type :os)
    "web.os.popups."
    (str "web.apps." (name app-type) ".popups.")))

(defn dispatch-popup-db
  [app-type popup-type fun & args]
  (he.dispatch/call
   (str (get-popup-prefix app-type) (name popup-type) ".db/" (name fun))
   ;(first (apply conj args))
   (apply conj args)
   ))

(defn dispatch-popup-view
  [app-type popup-type & args]
  (he.dispatch/call
   (str (get-popup-prefix app-type) (name popup-type) ".view/view")
   args))

(defn dispatch-popup-full-view
  [app-type popup-type & args]
  (he.dispatch/call
   (str (get-popup-prefix app-type) (name popup-type) ".view/full-view")
   args))
