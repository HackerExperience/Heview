(ns web.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]
            [web.ui.style]
            [web.home.style]
            [web.os.style]
            [web.wm.style]
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

(defn style
  []
  [(global-style)
   [(web.hud.style/local-style)]
   [(web.hud.style/global-style)]
   [(web.ui.style/local-style)]
   [(web.ui.style/global-style)]
   [(web.home.style/local-style)]
   [(web.home.style/global-style)]
   [(web.os.style/local-style)]
   [(web.os.style/global-style)]
   [(web.wm.style/local-style)]
   [(web.wm.style/global-style)]])

(def target-path "target/he.css")

(def top-level-style
  (css
   {:output-to target-path}
   (style)))
