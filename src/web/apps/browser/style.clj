(ns web.apps.browser.style
  (:require [web.ui.vars :as ui]
            [web.apps.browser.page.style :as browser.style.page]))

;; Attempt at CSS BEM (kinda)

(defn header []
  [[:.br-header
    {:min-height "35px"
     :border-bottom (str "1px solid " ui/color-primary)
     :background (ui/color-primary-darkest-rgba "0.25")
     :display :flex
     :flex-direction :row
     :align-items :center
     :padding "0 10px"}]
   [:.br-h-nav-buttons
    {:display :flex
     :flex-direction :row
     :min-width "90px"
     :align-items :center}]
   [:.br-h-nav-button
    {:padding "0 4px"
     :min-width "15px"
     :color (ui/color-primary-light-rgba "0.85")}
    [:&:hover
     {:color ui/color-primary-lighter
      :cursor :pointer}
     [:.br-h-nav-button-disabled
      {:color ui/color-primary
       :cursor :initial}]]]
   [:.br-h-nav-button-caret
    {:font-size "16px"}]
   [:.br-h-nav-button-disabled
    {:color ui/color-primary}]
   [:.br-h-nav-bar
    {:flex "1 1"
     :display :flex
     :flex-direction :row
     :align-items :center}]
   [:.br-h-nav-bar-input
    {:min-width "400px"
     :height "22px"
     :font-family :monospace}]
   [:.br-h-nav-bar-button
    {:min-width "20px"
     :text-align :center
     :font-size "20px"
     :padding "0 3px"}]])

(defn sidebar []
  [[:.br-sidebar
    {:width "170px"
     :display :flex
     :flex-direction :column
     :background (ui/color-primary-darkest-rgba "0.4")
     :border-right (str "1px solid " ui/color-primary)}]
   [:.br-s-tabs
    {:display :flex
     :flex-direction :column
     :height "300px"
     :overflow :auto
     ;; :padding-right "1px"
     }]
   [:.br-s-tab
    {:display :flex
     :min-height "32px"
     :flex-direction :row
     :align-items :center
     :border-bottom (str "1px solid " ui/color-primary-dark)
     :background (ui/color-primary-darkest-rgba "0.5")
     :padding "0 5px"
     :position :relative}
    [:&:hover
     {:cursor :pointer
      :background (ui/color-primary-darkest-rgba "0.9")}
     [:.br-s-tab-icon
      {:color ui/color-primary-light}]
     [:.br-s-tab-close
      [:i
       {:display :block}]]]]
   [:.br-s-tab-active
    {:background (ui/color-primary-darker-rgba "0.6")
     :font-weight :bold}
    [:&:hover
     {:background (ui/color-primary-darker-rgba "0.75")}]]
   [:.br-s-tab-icon
    {:text-align :center
     :min-width "25px"
     :color (ui/color-primary-light-rgba "0.8")}]
   [:.br-s-tab-title
    {:width "130px"
     :text-overflow :ellipsis
     :white-space :nowrap
     :overflow :hidden}]
   [:.br-s-tab-close
    {:position :absolute
     :padding "2px"
     :color ui/color-primary
     :right "3px"
     :top "7px"
     :min-width "13px"
     :min-height "20px"}
    [:i
     {:display :none}]
    [:&:hover
     [:i
      {:color ui/color-primary-light}]]]
   [:.br-s-new-tab
    {:margin "8px 10px 8px auto"
     :font-size "20px"}]
   [:.br-s-new-tab-button
    {:margin-left :auto
     :height "27px"
     :color (ui/color-primary-light-rgba "0.75")}]])

(defn viewport []
  [[:.br-viewport
    {:flex "1 1"
     :background (ui/color-background-rgba "0.35")
     :overflow-x :hidden
     :overflow-y :auto
     :position :relative}]
   [:.br-v-sb
    {:position :absolute
     :left "0"
     :bottom "0"
     :background ui/color-primary-darker
     :font-size "11px"
     :border-top (str "1px solid " ui/color-primary)
     :border-right (str "1px solid " ui/color-primary)
     :max-width "70%"
     :overflow :hidden
     :text-overflow :ellipsis
     :white-space :nowrap
     :padding "4px 4px 4px 4px"
     :display :none}]])

(defn body []
  [[:.br-body
    {:flex "1 1"
     :display :flex
     :flex-direction :row}]])

(defn local-style []
  [[:.app-type-browser
    {:height "100%"}]
   [:.br-container
    {:display :flex
     :flex-direction :column
     :height "100%"}]
   [(header)]
   [(body)]
   [(sidebar)]
   [(viewport)]
   [(browser.style.page/local-style)]])
