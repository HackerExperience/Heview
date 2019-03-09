(ns web.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]
            [web.ui.style]
            [web.home.style]
            [web.os.style]
            [web.wm.style]
            [web.hemacs.style]
            [web.hud.style]))

(defn global-style []
  [["*" "*::before" "*::after"
    {:box-sizing :border-box}]
   ["*"
    {:outline :none}]
   [:html
    {:line-height "1.2"}]
   [:button :input :optgroup :select :textarea
    {:line-height "1.2"}]
   ["input::placeholder"
    {:color ui/color-primary}]])

(defn style []
  [(global-style)
   [(web.hemacs.style/style)]
   [(web.hud.style/style)]
   [(web.ui.style/style)]
   [(web.home.style/style)]
   [(web.os.style/style)]
   [(web.wm.style/style)]])

(def target-path "target/he.css")

(def top-level-style
  (css
   {:output-to target-path}
   (style)))
