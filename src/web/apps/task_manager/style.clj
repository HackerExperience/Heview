(ns web.apps.task-manager.style
  (:require [web.ui.vars :as ui]))

(defn body []
  [:.tm-body
   {:flex "1 1"
    :background-color (ui/color-primary-darkest-rgba "0.05")
    :padding "8px 8px 0 8px"
    ;; :padding "8px"
    :overflow-y :auto}
   [:.tm-server-entry {}
    [:.tm-server-header
     {:border-bottom (str "1px dashed " ui/color-primary)
      :padding-bottom "2px"
      :margin-bottom "5px"}
     [:span
      {:font-size "18px"
       :font-weight :bold
       :font-family :monospace}]]
    [:.tm-server-processes
     {:display :flex
      :flex-direction :column}
     [:span.tm-server-processes-empty
      {:font-size "16px"
       :padding-top "3px"
       :color ui/color-primary}]]]
   [".tm-server-entry + .tm-server-entry"
    {:margin-top "4px"}]
   [:.tm-process-selected
    {:min-height "90px"
     :background-color (ui/color-primary-darkest-rgba "0.8")
     :border (str "1px solid " (ui/color-primary-dark-rgba "0.7"))
     :margin-bottom "4px"}
    [:&:hover
     {:background-color (ui/color-primary-darkest-rgba "0.9")
      :border (str "1px solid " (ui/color-primary-dark-rgba "0.8"))}]
    [:.tm-process-entry
     {:border "0"}
     [:&:hover
      {:border "0"
       :background-color :initial}]]
    [:.tm-process-selected-separator
     {:width "80%"
      :margin-left :auto
      :margin-right :auto
      :border-bottom (str "1px solid " ui/color-primary-dark)}]
    [:.tm-process-selected-action-area
     {:min-height "42px"
      :display :flex
      :flex-direction :row
      :align-items :center
      :justify-content :center}]]
   [:.tm-process-entry
    {:min-height "40px"
     :display :flex
     :flex-direction :row
     :align-items :center
     :border (str "1px solid " (ui/color-primary-darker-rgba "0.6"))
     :background-color (ui/color-primary-darkest-rgba "0.4")
     :margin-bottom "4px"
     :padding "3px 4px"}
    [:&:hover
     {:border (str "1px solid " (ui/color-primary-darker-rgba "0.8"))
      :background-color (ui/color-primary-darkest-rgba "0.65")}]
    [:.tm-process-icon
     {:flex "0 0"
      :text-align :center}
     [:i
      {:font-size "20px"}]]
    [:.tm-process-desc
     {:flex "0 0"
      :padding-left "8px"
      :min-width "150px"
      :display :flex
      :flex-direction :column
      :overflow :hidden}
     [:span.tm-process-desc-action
      {:font-size "15px"}]
     [:span.tm-process-desc-notes
      {:font-size "11px"
       :font-family :monospace
       :text-overflow :ellipsis
       :overflow :hidden
       :white-space :nowrap
       :color ui/color-secondary-light}]]
    [:.tm-process-info
     {:flex "1 1"
      :display :flex
      :flex-direction :row
      :align-items :center}
     [:.tm-process-info-eta
      {:display :flex
       :flex-direction :column
       :align-items :center
       :min-width "160px"
       :max-width "160px"
       :font-family :monospace}
      [:.tm-process-info-eta-bar
       {:position :relative
        :margin-top "1px"
        :min-width "120px"
        ;:max-width "100px"
        :min-height "12px"
        :border (str "1px solid " ui/color-primary-light)
        :background-color (ui/color-primary-darker-rgba "0.7")
        }
       [:.tm-process-info-eta-bar-progress
        {:min-height "12px"
         :background-color (ui/color-primary-rgba "0.5")
         :border-right (str "1px solid " ui/color-primary)}]
       [:span
        {:position :absolute
         :left "38%"
         :top "-2px"
         :text-shadow (str "0 0 " ui/color-primary-light)}]]
      [:span
       {:font-size "10px"
        :margin-top "3px"
        :overflow :hidden
        :text-overflow :ellipsis
        :white-space :nowrap}]]
     [:.tm-process-info-usage
      {:flex "1 1"
       :display :flex
       :flex-direction :column
       :text-align :right}
      [:.tm-process-info-usage-entry
       {:display :flex
        :flex-direction :row
         :color (ui/color-primary-light-rgba "0.825")
        :text-align :center}
       [:i
        {:font-size "12px"
         :margin-right "2px"
         :min-width "20px"
         }]
       [:span
        {:font-size "12px"
         :font-family :monospace}]]]]]])

(defn header []
  [:.tm-header
   {:flex "0 0"
    :min-height "33px"
    :display :flex
    :flex-direction :row
    :padding-bottom "8px"
    :background-color (ui/color-primary-darkest-rgba "0.215")
    :border-bottom (str "1px solid " ui/color-primary)}
   [:.tm-flag-area
    {:padding-left "8px"
     :flex "1 1"
     :height "100%"
     :align-self :center
     :display :flex
     :flex-direction :row}
    [:.tm-flag
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
    [:.tm-flag-disabled
     {:color ui/color-primary-dark}]
    ["div + div"
     {:margin-left "6px"}]]
   [:.tm-search-area
    {:flex "0 0"
     :display :flex
     :flex-direction :row
     :align-items :center
     :padding-right "8px"
     }
    [:.tm-search-filter
     {:flex "0 0"
      :margin-right "5px"
      :font-size "16px"}
     [:&:hover
      {:color ui/color-primary-lightest
       :cursor :pointer}]]
    [:.tm-search-input
     {:flex "1 1"
      :position :relative}
     [:input
      {:width "100px"}]
     [:i
      {:position :absolute
       :top "5px"
       :right "7px"}
      [:&:hover
       {:color ui/color-primary-lightest}]]]]])

(defn main []
  [:.tm-main
   {:flex "1 1"
    :display :flex
    :flex-direction :column
    :padding "8px 0 0 0"}
   [[(header)]
    [(body)]]])

(defn sidebar []
  [:.tm-sidebar
   {:flex "0 0"
    :min-width "160px"
    :display :flex
    :flex-direction :column
    :align-items :center
    :background-color (ui/color-primary-darkest-rgba "0.275")
    ;:border-right (str "1px solid " (ui/color-primary-darkest-rgba "0.95"))
    :border-right (str "1px solid " (ui/color-primary-darker-rgba "0.95"))
    :padding "0 8px"
    }
   [:.tm-sidebar-entry
    {:min-height "40px"
     :width "100%"
     :display :flex
     :flex-direction :row
     :align-items :center
     :padding "0 5px"
     :border "1px solid transparent"}
    [[:.tm-sidebar-entry-icon
      {:flex "0 0"
       :min-width "26px"}
      [:i
       {:font-size "22px"}]]
     [:.tm-sidebar-entry-name
      {:margin-left "2px"
       :flex "1 1"
       :font-size "16px"}]
     [:&:hover
      {:border (str "1px solid " ui/color-primary-darker)
       :background-color ui/color-primary-darkest
       :cursor :pointer}]
     [:&:first-child
      {:margin-top "8px"}]]]])

(defn local-style []
  [:.app-type-task-manager {}
   [:.tm-container
    {:height "100%"
     :display :flex
     :flex-direction :row}
    [[(sidebar)]
     [(main)]]]])
