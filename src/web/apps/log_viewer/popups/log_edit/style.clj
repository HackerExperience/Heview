(ns web.apps.log-viewer.popups.log-edit.style
  (:require [garden.core :refer [css]]
            [garden.selectors :refer [attr=]]))

(defn local-style []
  [(attr= :data-app-type :log-viewer-log-edit)
   {}
   [:.lv-led-container
    {:background "#000"
     :height "100%"
     :display :flex
     :flex-direction :column}]])
