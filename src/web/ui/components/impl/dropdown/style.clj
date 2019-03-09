(ns web.ui.components.impl.dropdown.style
  (:require [web.ui.vars :as ui]))

(defn local-style []
  [[:.ui-c-dd-container
    {:display :flex
     :flex-direction :column
     :align-items :center
     :border (str "1px solid " ui/color-primary)
     :min-height "30px"
     :min-width "150px"
     :background ui/color-primary-darkest
     :position :relative}
    [:&:hover
     {:background (ui/color-primary-dark-rgba "0.5")
      :border-color (ui/color-primary-light-rgba "0.45")}]]
   [".ui-c-dd-container[data-drop=\"true\"]"
    {:border-color ui/color-primary-lightest
     :background (ui/color-primary-dark-rgba "0.8")}]
   [:.ui-c-dd-selected-container
    {:display :flex
     :flex-direction :row
     :flex "1 1"
     :height "100%"
     :width "100%"
     :align-items :center
     :padding "5px"}
    [:&:hover
     {:cursor :pointer}]]
   [:.ui-c-dd-selected-entry
    {:flex "1 1"}]
   [:.ui-c-dd-selected-caret-area
    {:min-width "20px"
     :text-align :center}]
   [:.ui-c-dd-drop
    {:position :absolute
     :width "calc(100% + 2px)"
     :top "29px"
     :left "-1px"
     :background (ui/color-primary-darkest-rgba "0.95")
     :border-left (str "1px solid " ui/color-primary-light)
     :border-right (str "1px solid " ui/color-primary-light)
     :border-bottom (str "1px solid " ui/color-primary-light)
     :z-index 9999
     :display :flex
     :flex-direction :column}]
   [:.ui-c-dd-drop-search-container
    {:min-height "30px"
     :display :flex
     :flex-direction :row
     :background (ui/color-primary-darker-rgba "0.3")
     :position :relative
     :padding "10px 0 7px 0px"
     :border-bottom (str "1px solid " (ui/color-primary-rgba "0.5"))}]
   [:.ui-c-dd-drop-search-input
    {:flex "1 1"
     :background (ui/color-primary-darkest-rgba "0.5")
     :border (str "1px solid " ui/color-primary)
     :margin "0 10px"
     :min-height "25px"}]
   [:.ui-c-dd-drop-search-icon
    {:position :absolute
     :right "17px"
     :top "15px"}]
   [:.ui-c-dd-drop-entries
    {:display :flex
     :flex-direction :column
     :flex "1 1"
     ;; :background "#333"
     :padding "5px"
     :overflow-y :auto
     :overflow-x :hidden}]
   [:.ui-c-dd-drop-entries-empty
    {:padding "5px"}]
   [:.ui-c-dd-drop-entry
    {:min-height "30px"
     :padding-left "5px"
     :display :flex
     :flex-direction :row
     :align-items :center
     :border "1px solid transparent"}
    [:&:hover
     {:background (ui/color-primary-dark-rgba "0.8")
      :border (str "1px solid " ui/color-primary)
      :cursor :pointer}]]
   [:.ui-c-dd-drop-entry-selected
    {:background (ui/color-primary-dark-rgba "0.6")
     :border (str "1px solid " (ui/color-primary-rgba "0.5"))}
    [:&:hover
     {:background (ui/color-primary-dark-rgba "0.75")}]]
   [:.ui-c-dd-drop-entry-highlighted
    {:background (ui/color-primary-dark-rgba "0.9")
     }]
   [:.ui-c-dd-drop-group
    [:>.ui-c-dd-drop-entry
     {:padding-left "15px"}]]
   [:.ui-c-dd-drop-group-name
    {:min-height "30px"
     :padding-left "5px"
     :color ui/color-primary-lightest
     :font-weight :bold
     :display :flex
     :align-items :center}
    [:&:hover
     {:cursor :initial}]]
   ])
