(ns web.apps.log-viewer.style
  (:require [garden.core :refer [css]]
            [garden.selectors :refer [attr=]]
            [web.ui.vars :as ui]))

(defn header []
  [:.lv-header
   {:display :flex
    :flex-direction :row}
   [:.lv-flag-area
    {:flex "1 1"
     :height "100%"
     :align-self :center
     :display :flex
     :flex-direction :row}
    [:.lv-flag
     {:width "26px"
      :height "26px"
      :position :relative
      :text-align :center}
     [[:&:hover
       {:cursor :pointer}]
      [:i
       {:font-size "20px"
        :position :relative
        :margin-top "3px"}]]]
    [:.lv-flag-disabled
     {:color ui/color-primary-dark}]
    ["div + div"
     {:margin-left "6px"}]]
   [:.lv-search-area
    {:flex "0 0"
     :display :flex
     :flex-direction :row
     :align-items :center}
    [:.lv-search-filter
     {:flex "0 0"
      :margin-right "5px"
      :font-size "16px"}
     [:&:hover
      {:color ui/color-primary-lightest
       :cursor :pointer}]]
    [:.lv-search-input
     {:flex "1 1"
      :position :relative}
     [:input
      {:width "100px"}]
     [:i
      {:position :absolute
       :top "5px"
       :right "7px"}]]]])

(defn body []
  [:.lv-body
   {:height "100%"
    :overflow :auto}
   [:.lv-entries
    {:display "flex"
     :flex-direction "column"
     :height "100%"
     :overflow :auto}
    [:.lv-entry-container
     {:border-bottom (str "1px solid" ui/color-primary)
      :border-right-style :none
      :border-left-style :none}
     ["&:nth-child(odd)"
      {:background "rgba(13, 2, 8, 0.225)"}]
     ["&:nth-child(even)"
      {:background "rgba(0, 0, 0, 0.325)"}]
     ["&:nth-child(odd):hover"
      {:background (ui/color-primary-darkest-rgba "0.6")}]
     ["&:nth-child(even):hover"
      {:background (ui/color-primary-darkest-rgba "0.6")}]]
    [:.lv-entry
     {:padding "5px 15px"
      :display :flex
      :flex-direction :row
      :min-height "36px"}
     [:.lv-entry-date
      {:flex "0 0"
       :display :flex
       :flex-direction :column
       :justify-content :center
       :font-size "11px"
       :color ui/color-primary}
      [:.lv-entry-date-dmy
       {:flex "0 0"}]
      [:.lv-entry-date-hms
       {:flex "0 0"}]]
     [:.lv-entry-separator
      {:margin-left "7px"
       :border-left (str "1px solid " ui/color-primary-darker)}]
     [:.lv-entry-body
      {:flex "1 1"
       :margin-left "7px"
       :overflow :hidden
       :text-overflow :ellipsis
       :align-self :center
       :white-space :nowrap}]]
    ;; The `lv-selected` class modifies the behavior of some of the `lv-entry-*`
    ;; rules defined above. Keep this in mind and be happy!
    [:.lv-selected
     {:min-height "94px"
      :display :flex
      :flex-direction :column
      :background (ui/color-primary-darkest-rgba "0.75")}
     [[:&:hover
       {:background (ui/color-primary-darkest-rgba "0.85")}]
      [:.lv-entry
       {:border-bottom :none
        :flex-grow 1}
       [:.lv-entry-body
        {:white-space :normal}]]
      [:.lv-selected-separator
       {:width "80%"
        :margin-left :auto
        :margin-right :auto
        :border-top (str "1px solid " ui/color-primary-darker)}]
      [:.lv-selected-action-area
       {:min-height "42px"
        :display :flex
        :flex-direction :row
        :align-items :center
        :justify-content :center}]]]]])

(defn footer []
  [:.lv-footer
   {:display :flex
    :flex-direction :row
    :align-items :center}])

(defn local-style []
  [(attr= :data-app-type :log-viewer)
   {}
   [:.lv-container
    {:height "100%"
     :display :flex
     :flex-direction :column}
    [[(header)]
     [(body)]
     [(footer)]]]])

(defn global-style []
  [])
