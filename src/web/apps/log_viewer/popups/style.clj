(ns web.apps.log-viewer.popups.style
  (:require [web.apps.log-viewer.popups.log-edit.style :as log-edit.style]))

(defn style []
  [(log-edit.style/style)])
