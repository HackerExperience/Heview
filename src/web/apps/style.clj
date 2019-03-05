(ns web.apps.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]
            [web.apps.software.style :as software.style]
            [web.apps.file-explorer.style :as fe.style]
            [web.apps.log-viewer.style :as lv.style]
            [web.apps.log-viewer.popups.style :as lv.popups.style]
            [web.apps.remote-access.style :as ra.style]
            [web.apps.task-manager.style :as tm.style]))

(defn local-style []
  [(software.style/local-style)
   (fe.style/local-style)
   (lv.style/local-style)
   (lv.popups.style/local-style)
   (ra.style/local-style)
   (tm.style/local-style)])

(defn global-style []
  [[:.ui-app-header
    {:background (ui/color-primary-darkest-rgba "0.215")
     :min-height "42px"
     :border-bottom (str "1px solid" ui/color-primary-light)
     :padding "5px 15px"}]
   [:.ui-app-footer
    {:background (ui/color-primary-darkest-rgba "0.215")
     :min-height "42px"
     :border-top (str "1px solid" ui/color-primary-light)
     :padding "5px 15px"}]])
