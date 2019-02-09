(ns web.wm.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]
            [web.apps.style :as apps.style]))

(defn local-style []
  [])

(defn global-style []
  [[:#wm
    {:height "100%"}
    [:.app
     {:border (str "1px solid" ui/color-primary-lightest)
      :border-radius "0px"
      :box-shadow (str
                   "0 0 1px 1px " (ui/color-primary-light-rgba "0.25")
                   ","
                   "0 0 4px 4px rgba(0,0,0,0.4)"
                   ","
                   "0 0 8px 8px rgba(0,0,0,0.25)")
      :position :absolute
      :transform "translateZ(0)"}
     [:.app-container
      {:display :flex
       :flex-direction :column
       :height "100%"
       :width "100%"
       :color ui/color-primary-light
       :background-image ui/background-dotie-url
       :background-color "rgba(0, 16, 20, 0.785)"
       :position :relative
       }]]
    [:.app-header
     {:display :flex
      :z-index 1
      :flex "0"
      :min-height "30px"
      :position :relative
      :align-items :center
      :border-bottom (str "1px solid" ui/color-primary-lightest)
      :background-color ui/color-primary-darkest}
     [:.app-header-icon
      {:color ui/color-primary-light
       :margin-left "10px"
       :flex-basis "1em"
       :align-self :center}]
     [:.app-header-title
      {:flex-grow "1"
       :overflow :hidden
       :white-space :nowrap
       :text-align :center
       :color ui/color-primary-light
       :font-weight :bold}]
     [:.app-header-actions
      {:flex-basis "3em"
       :display :inline-flex
       :z-index 2
       :margin-bottom "3px"
       :min-width "1em"
       :position :relative}
      [:.app-header-action
       {:flex-basis "1em"
        :border "1px solid hsla(0,0%,40%,.5)"
        :width "1em"
        :height "1em"
        :margin-left ".1em"
        :border-radius "100%"}]
      [:.app-header-action-minimize
       {:background "#abcdef"}]
      [:.app-header-action-close
       {:background "#e56c5c"}]]]
    [:.app-body
     {:flex "1"
      ;; Must be `max-height`. `height` will not work on Firefox
      :max-height "calc(100% - 30px)"
      :position :relative}]
    [:.app-vibrate
     {:border (str "1px solid" ui/color-primary)}]
    [:.app-moving
     {:cursor :move}]
    [:.app-focused
     {:background-color "rgba(10, 26, 30, 0.825)"
      :box-shadow (str
                   "0 0 2px 2px " (ui/color-primary-rgba "0.5")
                   ","
                   "0 0 4px 4px " (ui/color-primary-rgba "0.25"))}]
    [:.full-app
     {:position :absolute
      :transform "translateZ(0)"}
     [:.full-app-container
      {:height "100%"
       :width "100%"}]]
    [(apps.style/local-style)]]
   (apps.style/global-style)])
