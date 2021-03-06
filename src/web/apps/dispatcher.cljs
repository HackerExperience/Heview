(ns web.apps.dispatcher
  (:require [he.dispatch]
            [web.apps.software.view]
            [web.apps.browser.view]
            [web.apps.chat.view]
            [web.apps.file-explorer.view]
            [web.apps.log-viewer.view]
            [web.apps.remote-access.view]
            [web.apps.task-manager.view]))

(defn- get-software-name
  [app-type]
  (let [[_ & type-suffix] (.split (name app-type) "-")]
    (clojure.string/join "-" type-suffix)))

(defn- get-app-software-prefix
  [app-type]
  (str "web.apps.software." (get-software-name app-type)))

(defn- get-app-prefix
  [app-type]
  (cond
    (= app-type :os) "web.os.popups"
    (.test #"software-" (str app-type)) (get-app-software-prefix app-type)
    :else (str "web.apps." (name app-type))))

(defn- dispatch-db-default
  [app-type fun args]
  (he.dispatch/call (str "web.apps.db/default-" (name fun)) [app-type args]))

(defn dispatch-db
  [app-type fun & args]
  (let [fun-path (str (get-app-prefix app-type) ".db/" (name fun))
        fun-args (apply conj args)]
    (if (he.dispatch/function-exists? fun-path)
      (he.dispatch/call fun-path fun-args)
      (dispatch-db-default app-type fun fun-args))))

(defn dispatch-view
  [app-type & args]
  (he.dispatch/call (str (get-app-prefix app-type) ".view/view") args))

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
   (apply conj args)))

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
