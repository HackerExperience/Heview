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
     {:border (str "1px solid " ui/color-primary-lightest)
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
      :flex-direction :row
      :z-index 1
      :flex "0"
      :min-height "30px"
      :position :relative
      :align-items :center
      :border-bottom (str "1px solid " ui/color-primary-lightest)
      :background-color ui/color-primary-darkest}
     [:.app-header-seq-id
      {:margin-left "7px"
       :margin-right "-3px"
       :border (str "1px solid " ui/color-secondary)
       :background-color ui/color-secondary-darkest;
       :color ui/color-secondary-light
       :padding "1px 3px"
       }]
     [:.app-header-icon
      {:color ui/color-primary-light
       :margin-left "10px"
       :align-self :center}]
     [:.app-header-icon-separator
      {:margin-left "6px"
       :margin-right "6px"
       :border-left (str "1px solid " ui/color-primary)
       :height "50%"}]
     [:.app-header-title
      {:overflow :hidden
       :flex "1 1"
       :margin-right "0"
       :white-space :nowrap
       :color ui/color-primary-light
       :text-overflow :ellipsis
       :font-weight :bold}]
     [:.app-header-context
      {:display :flex
       :flex-direction :row
       :margin-right "10px"
       :background ui/color-primary-dark
       :color (ui/color-primary-light-rgba "0.725")
       :padding "0px 2px"
       :border (str "1px solid " ui/color-primary)}
      [:span
       {:font-size "10px"
        :text-shadow (str "0 0 " ui/color-primary-light)
        :margin-top "3px"}]
      [:i
       {:font-size "10px"
        :align-self :center
        :margin-left "3px"}]
      [:&:hover
       {:color ui/color-primary-lightest
        :border (str "1px solid " ui/color-primary-light)
        :cursor :pointer}]]
     [:.app-header-context-disabled
      [:i
       {:display :none}]
      [:&:hover
       {:cursor :initial
        :color (ui/color-primary-light-rgba "0.725")
        :border (str "1px solid " ui/color-primary)}]]
     [:.app-header-file
      {:display :flex
       :flex-direction :row
       :align-items :center
       :border (str "1px solid " ui/color-secondary)
       :background (ui/color-secondary-darkest-rgba "0.40")
       :color ui/color-secondary-light
       :margin "0 3px"}
      [:&:hover
       {:border (str "1px solid " ui/color-secondary-light)
        :cursor :pointer}
       [:.app-header-file-name
        {:color ui/color-secondary-lightest}]
       [:.app-header-file-selector
        {:color ui/color-secondary-lightest}]
       [:.app-header-file-modules
        [:.app-header-file-module
         {:color ui/color-secondary-light}]]]
      [:.app-header-file-name
       {:font-size "11px"
        :padding "0 3px"}]
      [:.app-header-file-modules
       {:display :flex
        :flex-direction :column
        :padding "1px 2px"
        :min-height "22px"
        :align-items :center
        :justify-content :center}
       [:.app-header-file-module
        {:display :flex
         :flex-direction :row-reverse
         :min-width "39px"
         :color ui/color-secondary}
        [:i
         {:font-size "9px"
          :width "11px"}]
        [:span
         {:font-size "9px"
          :font-family :monospace
          }]]]
      [:.app-header-file-selector
       {:font-size "12px"
        :padding "0 1px 0 3px"}
       [:i
        {}]]]
     [:.app-header-actions
      {:display :flex
       :z-index 2
       :margin-right "5px"
       :margin-left :auto
       :position :relative
       :color (ui/color-primary-light-rgba "0.700")}
      [:.app-header-action
       {:font-size "16px"
        :margin-left "3px"}
       [:i
        [:&:hover
         {:color ui/color-primary-light
          :cursor :pointer}]]]]]
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
