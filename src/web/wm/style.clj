(ns web.wm.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]
            [web.apps.style :as apps.style]))

(defn local-style []
  [])

(defn app-header-file []
  [[:.wm-a-h-file-dd-container
    {:border (str "1px solid " ui/color-secondary)
     :color ui/color-secondary-light
     :background (ui/color-secondary-darkest-rgba "0.4")
     :min-height :unset
     :min-width :unset}
    [:&:hover
     {:background (ui/color-secondary-darkest-rgba "0.5")
      :border (str "1px solid " ui/color-secondary-light)}]]
   [".wm-a-h-file-dd-container[data-drop=\"true\"]"
    {:border-color ui/color-secondary-light
     :background (ui/color-secondary-darkest-rgba "0.8")
     :min-width "200px"}]
   [:.wm-a-h-file-dd-selected-container
    {:padding "0"}]
   [:.wm-a-h-file-dd-selected-entry
    {:display :flex
     :flex-direction :row
     :align-items :center
     :margin "0 3px"}]
   [:.wm-a-h-file-dd-selected-entry-name
    {:font-size "11px"
     :flex "1 1"
     :padding "0 3px 0 12px"}]
   [:.wm-a-h-file-dd-selected-entry-modules
    {:display :flex
     :flex-direction :column
     :padding "1px 2px"
     :min-height "22px"
     :align-items :center
     :justify-content :center
     :font-size "9px"
     :font-family :monospace}]
   [:.wm-a-h-file-dd-selected-entry-module
    {:display :flex
     :flex-direction :row-reverse
     :min-width "39px"
     :color ui/color-secondary}
    [:>i
     {:width "11px"}]
    [:>span
     {:padding-left "1px"}]]
   [:.wm-a-h-file-dd-selected-caret-area
    {:position :absolute
     :left "0"
     :top "4px"
     :min-width "15px"
     :font-size "12px"}]
   [:.wm-a-h-file-dd-drop
    {:top "23px"
     :background (ui/color-primary-darkest-rgba "0.95")
     :border-left (str "1px solid " ui/color-secondary-light)
     :border-right (str "1px solid " ui/color-secondary-light)
     :border-bottom (str "1px solid " ui/color-secondary-light)}]
   [:.wm-a-h-file-dd-drop-entries
    [:>.ui-c-dd-drop-entry-highlighted
     {:background (ui/color-secondary-dark-rgba "0.25")}]]
   [:.wm-a-h-file-dd-drop-entry
    {:padding-left "2px"}
    [:&:hover
     {:background (ui/color-secondary-dark-rgba "0.15")
      :border (str "1px solid " ui/color-secondary)}]]
   [:.wm-a-h-file-dd-drop-entry-selected
    {:background (ui/color-secondary-dark-rgba "0.4")
     :border (str "1px solid " (ui/color-secondary-rgba "0.5"))}
    [:&:hover
     {:background (ui/color-secondary-dark-rgba "0.55")
      :border (str "1px solid " ui/color-secondary)}]]
   [:.wm-a-h-file-dd-drop-entry-name
    {:flex "1 1"
     :overflow :hidden
     :text-overflow :ellipsis
     :white-space :no-break
     }]
   [:.wm-a-h-file-dd-drop-entry-modules
    {:display :flex
     :flex-direction :row
     :font-size "10px"
     :font-family :monospace
     :min-width "82px"}]
   [:.wm-a-h-file-dd-drop-entry-module
    {:display :flex
     :flex-direction :row-reverse}
    [:>i
     {:padding "0 2px"}]]
   [".wm-a-h-file-dd-drop-entry-module:last-child"
    {:padding-right "2px"}]

   ])

(defn app-header []
  [[(app-header-file)]])

(defn global-style []
  [[(app-header)]
   [:#wm
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
