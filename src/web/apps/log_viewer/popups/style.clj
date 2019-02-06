(ns web.apps.log-viewer.popups.style
  (:require
   [web.apps.log-viewer.popups.log-edit.style :as lv.popups.log-edit.style]))

(defn local-style []
  [(lv.popups.log-edit.style/local-style)])
