(ns web.wm.windowable
  (:require [clojure.string :as str]
            [web.apps.dispatcher :as apps.dispatcher]))

(defn dispatch-db
  [method type & args]
  (apps.dispatcher/dispatch-db type method args))

(defn dispatch-popup-db
  [method app-type popup-type & args]
  (apps.dispatcher/dispatch-popup-db app-type popup-type method args))

;; Apps

(defn will-open
  [type ctx args]
  (dispatch-db :will-open type ctx args))
(defn did-open
  [type ctx args]
  (dispatch-db :did-open type ctx args))

(defn will-close
  [type ctx app-id state & args]
  (dispatch-db :will-close type ctx app-id state args))
(defn did-close
  [type ctx app-id state & args]
  (dispatch-db :did-close type ctx app-id state args))

(defn will-focus
  [type ctx app-id state & args]
  (dispatch-db :will-focus type ctx app-id state args))
(defn did-focus
  [type ctx app-id state & args]
  (dispatch-db :did-focus type ctx app-id state args))

;; Popup

(defn popup-may-open
  [type popup-type ctx parent-id args xargs]
  (dispatch-db :popup-may-open type ctx popup-type parent-id args xargs))
(defn popup-will-open
  [app-type popup-type ctx parent-id args xargs]
  (dispatch-popup-db :popup-will-open app-type popup-type ctx parent-id args xargs))
(defn popup-did-open
  [app-type popup-type ctx parent-id args xargs]
  (dispatch-popup-db :popup-did-open app-type popup-type ctx parent-id args xargs))

(defn popup-may-close
  [type popup-type ctx family-ids state args]
  (dispatch-db :popup-may-close type ctx popup-type family-ids state args))
(defn popup-will-close
  [type popup-type ctx family-ids state args xargs]
  (dispatch-popup-db :popup-will-close type popup-type ctx family-ids state args xargs))
(defn popup-did-close
  [type popup-type ctx family-ids state args xargs]
  (dispatch-popup-db :popup-did-close type popup-type ctx family-ids state args xargs))

(defn popup-may-focus
  [type popup-type ctx family-ids state args]
  (dispatch-db :popup-may-focus type ctx popup-type family-ids state args))
(defn popup-will-focus
  [type popup-type ctx family-ids state args xargs]
  (dispatch-popup-db :popup-will-focus type popup-type ctx family-ids state args xargs))
(defn popup-did-focus
  [type popup-type ctx family-ids state args xargs]
  (dispatch-popup-db :popup-did-focus type popup-type ctx family-ids state args xargs))
