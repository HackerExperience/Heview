(ns web.apps.file-explorer.style
  (:require [web.ui.vars :as ui]))

(defn header []
  [:.fe-header
   {:min-height "42px"
    :background-color (ui/color-primary-darkest-rgba "0.215")
    :border-bottom (str "1px solid " ui/color-primary)
    :display :flex
    :flex-direction :row
    :align-items :center
    :padding "0 5px"}
   [:.fe-flag-area
    {:flex "1 1"
     :display :flex
     :flex-direction :row}
    [:.fe-flag
     {:width "26px"
      :height "26px"
      :position :relative
      :text-align :center
      :cursor :pointer}
     [:i
      {:font-size "20px"
       :position :relative
       :margin-top "3px"}]]
    [:.fe-flag-disabled
     {:color ui/color-primary-dark}]]
   [:.fe-search-area
    {:flex "0 0"
     :display :flex
     :flex-direction :row
     :align-items :center}
    [:.fe-search-filter
     {:flex "0 0"
      :margin-right "5px"}
     [:&:hover
      {:color ui/color-primary-lightest
       :cursor :pointer}]]
    [:.fe-search-input
     {:flex "1 1"
      :position :relative}
     [:input
      {:width "120px"}]
     [:i
      {:position :absolute
       :top "5px"
       :right "7px"}
      [:&:hover
       {:color ui/color-primary-lightest}]]]]])

(defn body []
  [:.fe-body
   {:flex "1 1"
    :background-color (ui/color-primary-darkest-rgba "0.05")
    :overflow-y :auto}
   [:.fe-flat-view
    {:display :flex
     :flex-direction :column
     :padding "5px 0 5px 5px"
     :height "100%"}]
   [:.fe-file-entries
    {:overflow :auto
     :padding "0 5px 0 0"}]
   [:.fe-file
    {:display :flex
     :flex-direction :row
     :min-height "26px"
     :font-family :monospace
     :background-color (ui/color-primary-darkest-rgba "0.4")
     :border (str "1px solid " ui/color-primary-darker)
     :align-items :center
     :margin-top "3px"}
    [:&:hover
     {:background-color (ui/color-primary-darkest-rgba "0.65")
      :border (str "1px solid " ui/color-primary-dark)}
     [:.fe-file-icon
      {:color ui/color-primary-light}]
     [:.fe-file-module-icon
      {:color ui/color-primary-light}]
     [:.fe-file-module-version
      {:color ui/color-primary-light}]]
    [:.fe-file-icon
     {:flex "0 0"
      :min-width "20px"
      :text-align :center
      :color (ui/color-primary-light-rgba "0.75")}]
    [:.fe-file-name
     {:flex "0 0"
      :min-width "180px"
      :padding-left "1px"}]
    [:.fe-file-module-area
     {:flex "1 1"
      :display :flex
      :flex-direction :row
      :padding-left "3px"}
     [:.fe-file-module
      {:display :flex
       :flex-direction :row
       :min-width "60px"
       :color (ui/color-primary-light-rgba "0.5")
       :align-items :center}
      [:.fe-file-module-icon
       {:font-size "10px"
        :margin-top "2px"}]
      [:.fe-file-module-version
       {:margin "0 2px"}]]]]
   [:.fe-file-selected
    {:display :flex
     :flex-direction :column
     :font-family :monospace
     :background-color (ui/color-primary-darkest-rgba "0.8")
     :border (str "1px solid " (ui/color-primary-rgba "0.9"))
     :margin-top "3px"}
    [:&:hover
     {:background-color (ui/color-primary-darkest-rgba "0.9")
      :border (str "1px solid " ui/color-primary)}]
    [:.fe-file-selected-top
     {:flex "0 0"
      :min-height "26px"
      :display :flex
      :flex-direction :row
      :align-items :center}
     [:.fe-file-selected-icon
      {:min-width "20px"
       :text-align :center}]
     [:.fe-file-selected-name
      {:min-width "180px"
       :padding-left "1px"}]
     [:.fe-file-selected-type
      {:margin "-3px 7px 0 auto"
       :font-family ui/font-family
       :color ui/color-secondary}]]
    [:.fe-file-selected-module-area
     {:position :relative
      :flex "1 1"
      :display :flex
      :flex-direction :column
      :margin-left "10%"
      :min-height "35px"}
     [:.fe-file-selected-module
      {:display :flex
       :flex-direction :row
       :line-height "18px"}
      [:.fe-file-selected-module-icon
       {:min-width "20px"
        :text-align :center
        :font-size "10px"
        :color (ui/color-primary-light-rgba "0.8")}]
      [:.fe-file-selected-module-name
       {:min-width "100px"
        :text-align :right
        :margin-right "7px"
        :margin-top "-1px"
        :font-size "12px"
        :font-family ui/font-family}]
      [:.fe-file-selected-module-version
       {:font-size "12px"}]]]
    [:.fe-file-selected-side
     {:position :absolute
      :right "0"
      :top "0"
      :display :flex
      :flex-direction :column}
     [:.fe-file-selected-side-entry
      {:display :flex
       :flex-direction :row-reverse
       :align-items :center}
      [:.fe-file-selected-side-entry-text
       {:margin-right "4px"
        :font-size "11px"}]
      [:.fe-file-selected-side-entry-icon
       {:margin-right "7px"
        :font-size "12px"
        :min-width "15px"
        :text-align :center
        :color (ui/color-primary-light-rgba "0.8")}]
      ]]
    [:.fe-file-selected-separator
     {:width "90%"
      :margin "3px auto"
      :border-bottom (str "1px solid " ui/color-primary-dark)}]
    [:.fe-file-selected-action-area
     {:flex "0 0"
      :min-height "42px"
      :display :flex
      :flex-direction :row
      :align-items :center
      :justify-content :center}]]])

(defn main []
  [:.fe-main
   {:flex "1 1"
    :display :flex
    :flex-direction :column}
   [[(header)]
    [(body)]]])

(defn sidebar []
  [:.fe-sidebar
   {:min-width "180px"
    :display :flex
    :flex-direction :column
    :border-right (str "1px solid " (ui/color-primary-darker-rgba "0.95"))
    :background-color (ui/color-primary-darkest-rgba "0.275")}
   [:.fe-sidebar-storage-area
    {:flex "1 1"
     :margin-top "4px"
     :display :flex
     :flex-direction :column
     :overflow :auto}
    [:.fe-storage-entry
     {:min-height "50px"
      :display :flex
      :flex-direction :row
      :align-items :center
      :margin "2px 6px"
      :padding "4px"
      :border (str "1px solid " ui/color-primary-darker)
      :background-color (ui/color-primary-darkest-rgba "0.35")}
     [:&:hover
      {:cursor :pointer
       :border (str "1px solid " ui/color-primary-dark)
       :background-color (ui/color-primary-darkest-rgba "1.0")}]]
    [:.fe-storage-entry-selected
     {:background-color ui/color-primary-darkest
      :border (str "1px solid " ui/color-primary-dark)}
     [:&:hover
      {:border (str "1px solid " (ui/color-primary-rgba "0.5"))
       :background-color (ui/color-primary-darker-rgba "0.5")}]]
    [:.fe-storage-entry-icon
     {:min-width "30px"
      :padding "0 3px"
      :text-align :center}
     [:i
      {:font-size "30px"
       :color ui/color-primary}]]
    [:.fe-storage-entry-desc
     {:flex "1 1"
      :display :flex
      :flex-direction :column
      :font-family :monospace
      :margin-left "5px"}
     [:.fe-storage-entry-desc-name
      {:font-size "16px"}]
     [:.fe-storage-entry-desc-info
      {:font-size "10px"
       :margin-top "3px"
       :color ui/color-secondary}]]]
   [:.fe-sidebar-action-area
    {:min-height "80px"
     :display :flex
     :flex-direction :column-reverse
     :align-items :center}
    [:.fe-sidebar-action-button
     {:margin-bottom "10px"}
     [:button
      {:height "25px"}]]]
   [:.fe-sidebar-view-area
    {:min-height "40px"
     :display :flex
     :flex-direction :row
     :align-items :center
     :border-top (str "1px solid " (ui/color-primary-dark-rgba "0.70"))
     :background-color (ui/color-primary-darker-rgba "0.35")}
    [:&:hover
     {:cursor :pointer}]
    [:.fe-sidebar-view-selected
     {:background-color (ui/color-primary-darker-rgba "0.55")}];
    [:.fe-sidebar-view-tree
     {:flex "1 1"
      :display :flex
      :align-items :center
      :justify-content :center
      :height "40px"
      :border-right (str "1px solid " (ui/color-primary-dark-rgba "0.70"))}
     [:&:hover
      {:background-color (ui/color-primary-darker-rgba "0.85")}]]
    [:.fe-sidebar-view-flat
     {:flex "1 1"
      :display :flex
      :height "40px"
      :align-items :center
      :justify-content :center}
     [:&:hover
      {:background-color (ui/color-primary-darker-rgba "0.85")}]]
    [:i
     {:font-size "30px"}]]])

(defn local-style []
  [:.fe-container
   {:display :flex
    :flex-direction :row
    :height "100%"}
   [[(sidebar)]
    [(main)]]])
