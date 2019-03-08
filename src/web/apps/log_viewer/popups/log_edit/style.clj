(ns web.apps.log-viewer.popups.log-edit.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]))

(defn body []
  [:.lv-led-body
   {:flex "1 1"
    :display :flex
    :flex-direction :column}
   [:.lv-led-preview-area
    {:padding "10px 15px"
     :min-height "100px"
     :display :flex}
    [[:fieldset
      {:flex "1 1"
       :background (ui/color-primary-darkest-rgba "0.5")
       :border (str "1px solid " ui/color-primary-dark)
       :display :flex
       :min-height "100%"
       :font-size "12px"}]
     [:.lv-led-preview-area-corrupt
      {:border (str "1px solid " ui/color-secondary-dark)}
      [:legend
       {:color ui/color-secondary-light}]]
     [:.lv-led-preview-wrapper
      {:padding "5px 3px"
       :flex "1 1"
       :height "54px"
       :display :flex
       :flex-direction :row
       :align-items :center}
      [:.lv-led-preview-icon
       {:flex "0 0"
        :height "100%"}
       [:i
        {:font-size "20px"
         :margin-top "13px"}]
       [:i.lv-led-preview-icon-success
        {:color ui/color-primary}
        [:&:hover
         {:color ui/color-primary-light}]]
       [:i.lv-led-preview-icon-corrupt
        {:color ui/color-secondary}
        [:&:hover
         {:color ui/color-secondary-light}]]]
      [:.lv-led-preview-text
       {:flex "1 1"
        :font-size "13px"
        :height "100%"
        :margin-left "8px"
        :display :flex
        :align-items :center
        :overflow-y :auto
        :word-break :break-word}]]]]
   [:.lv-led-section-separator
    {:height "1px"
     :width "100%"
     :margin-left :auto
     :margin-right :auto
     :margin-top "5px"
     :margin-bottom "5px"
     :border-top (str "1px dotted" ui/color-primary)}]
   [:.lv-led-edit-area
    {:height "100%"
     :padding "5px 15px"
     :display :flex
     :flex-direction :column}
    [:.lv-led-edit-type
     {:min-height "40px"
      :flex "0 0"
      :display :flex
      :flex-direction :row
      :align-items :center}
     [:span
      {:min-width "90px"}]
     [:.lv-led-edit-type-select
      {:width "100%"
       :margin-right "2px"
       :margin-left :auto
       :display :flex
       :flex-direction :row
       :justify-content :flex-end}]]
    [:.lv-led-edit-fields
     {:flex "1 1"
      :margin-top "6px"
      :display :flex
      :flex-direction :column}
     [[:fieldset
       {:border (str "1px solid " ui/color-primary-dark)}]
      [:.lv-led-edit-field-row
       {:min-height "34px"
        :display :flex
        :flex-direction :row
        :align-items :center}
       [[:&:hover
         {:background (ui/color-primary-darkest-rgba "0.5")}]
        [:.lv-led-edit-field-help
         {:margin-right "2px"
          :cursor :initial}]
        [:.lv-led-edit-field-text
         {:width "150px"
          :margin-left "2px"}]
        [:.lv-led-edit-field-input-area
         {:position :relative}
         [:.lv-led-edit-field-input
          {:margin-right "10px"}]
         [:.field-content-invalid
          {:border (str "1px solid " ui/color-secondary-light)}
          [:&:hover
           {:border (str "1px solid " ui/color-secondary-lighter)}]]
         [:i
          {:position :absolute
           :color ui/color-secondary-light
           :right "17px"
           :top "5px"}
          [:&:hover
           {:cursor :initial
            :color ui/color-secondary-lightest}]]
         ]]]]
     [".lv-led-edit-field-row + .lv-led-edit-field-row"
      {:margin-top "10px"}]]]

   ;; Refactor ready

   ;; Dropdown
   [:.lv-led-edit-type-dd-container
    {:min-width "250px"
     }]
   [:.lv-led-edit-type-dd-selected
    {}]
   ])

(defn footer []
  [:.lv-led-footer
   {:flex "0 0"
    :display :flex
    :flex-direction :row
    :align-items :center}
   [:.lv-led-alternative-buttons
    {:flex "0 0"
     :min-width "140px"}
    ["button + button"
     {:margin-left "10px"}]]
   [:.lv-led-main-buttons
    {:flex "1 1"
     :display :flex
     :flex-direction :row
     :justify-content :flex-end}
    ["button + button"
     {:margin-left "10px"}]]

   ])

(defn local-style []
  [:.app-type-log-viewer-log-edit
   {}
   [:.lv-led-container
    {:height "100%"
     :display :flex
     :flex-direction :column}
    [[(body)]
     [(footer)]]]])
