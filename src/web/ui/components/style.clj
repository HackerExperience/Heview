(ns web.ui.components.style
  (:require [web.ui.components.impl.dropdown.style :as dropdown]
            [web.ui.components.impl.notification-panel.style :as np]))

(defn style []
  [[(dropdown/style)]
   [(np/style)]])
