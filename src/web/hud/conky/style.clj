(ns web.hud.conky.style
  (:require [web.ui.vars :as ui]
            [web.hud.conky.widgets.style :as widgets.style]))

(def header-size 168)
(def footer-size 110)

(defn header []
  [[:.hud-ck-h-player
    {:display :flex
     :flex-direction :column
     :align-items :center}]
   [:.hud-ck-h-p-avatar
    {:background "#000"
     :border (str "1px solid " ui/color-primary)
     :height "80px"
     :width "80px"}]
   [:.hud-ck-h-p-name
    {:padding-top "3px"
     :font-size "15px"}]
   [:.hud-ck-h-p-info
    {:display :flex
     :flex-direction :row
     :align-items :center
     :justify-content :center
     :text-align :center
     :width "250px"
     :font-size "11px"
     :font-family ui/font-monospace
     :padding "2px 0"}]
   [".hud-ck-h-p-info-entry + .hud-ck-h-p-info-entry"
    {:padding-left "5px"}]
   [:.hud-ck-h-p-info-entry-icon
    {:padding-right "3px"}]
   [:.hud-ck-h-p-info-separator
    {:height "6px"
     :width "6px"
     :display :inline-block
     :border (str "1px solid " ui/color-primary-dark)
     :background-color ui/color-primary
     :margin "0 5px"}]
   [:.hud-ck-h-actions
    {:display :flex
     :flex-direction :row
     :align-items :center
     :width "190px"
     :justify-content :space-between
     :padding-top "5px"
     :margin "0 auto"}]
   [:.hud-ck-h-a-action
    {:display :flex
     :align-items :center
     :justify-content :center
     :border (str "1px solid " ui/color-primary-dark)
     :background-color (ui/color-primary-darkest-rgba "0.5")
     :color (ui/color-primary-light-rgba "0.75")
     :font-size "18px"
     :height "30px"
     :width "30px"
     :padding "3px"}
    [:&:hover
     {:cursor :pointer
      :border (str "1px solid " ui/color-primary)
      :background-color (ui/color-primary-darkest-rgba "0.8")
      :color ui/color-primary-light}]]])

(defn widget-area []
  [[:.hud-ck-wa-widget
    {:display :flex
     :flex-direction :column
     :flex-shrink "0"}]
   [:.hud-ck-wa-w-header
    {:margin "5px 0 2px 0"
     :min-height "14px"
     :max-height "14px"
     :background (str "linear-gradient(90deg, "
                      "rgba(0,0,0,0) 0%, "
                      "rgba(0,0,0,0) 22%, "
                      (ui/color-primary-dark-rgba "0.5") " 50%, "
                      (ui/color-primary-dark-rgba "1") " 100%)")
     :position :relative
     :border (str "1px solid " ui/color-primary)}
    [:>span
     {:padding "2px 0 0 3px"}]]
   [:.hud-ck-wa-w-body
    {:flex "1"}]])

(defn footer []
  [[:.hud-ck-f-main
    {:flex "1 1"
     :display :flex
     :flex-direction :column
     :align-items :center
     :justify-content :center}]
   [:.hud-ck-f-m-time
    {:font-size "36px"}]
   [:.hud-ck-f-m-date
    {:font-size "16px"}]
   [:.hud-ck-f-footer
    {:display :flex
     :flex-direction :row
     :align-items :center
     :padding "0 5px"}]
   [:.hud-ck-f-f-launcher
    {:flex "1 1"
     :padding "5px"
     :display :flex
     :flex-direction :row}]
   [:.hud-ck-f-f-launcher-entry
    {:height "20px"
     :width "20px"
     :font-size "12px"
     :display :flex
     :align-items :center
     :justify-content :center
     :border (str "1px solid " ui/color-primary-dark)}
    [:&:hover
     {:border (str "1px solid " ui/color-primary)
      :cursor :pointer}]]
   [".hud-ck-f-f-launcher-entry + .hud-ck-f-f-launcher-entry"
    {:margin-left "5px"}]
   [:.hud-ck-f-f-version
    {:position :absolute
     :right "7px"
     :bottom "7px"
     :font-size "9px"
     :font-family ui/font-monospace
     :color ui/color-primary}]])

(defn ckui []
  [[:.hud-ckui-label
    {:color ui/color-primary-light
     :padding-right "5px"}]
   [:.hud-ckui-item
    {:color (ui/color-primary-light-rgba "0.75")}]
   [:.hud-ckui-subitem
    {:color ui/color-primary}]
   [:.hud-ckui-bar
    {:display :inline-flex
     :height "8px"
     :width "100%"
     :border (str "1px solid " ui/color-primary-dark)
     :background (ui/color-primary-darkest-rgba "1.0")}]
   [:.hud-ckui-bar-progress
    {:border-right (str "1px solid " (ui/color-primary-rgba "0.5"))
     :background (ui/color-primary-dark-rgba "0.8")}]
   [:.hud-ckui-row
    {:display :flex
     :flex-direction :row
     :align-items :center}]
   [:.hud-ckui-row-hoverable:hover
    {:background (ui/color-primary-darkest-rgba "0.8")}]
   [:.hud-ckui-button
    {:height "16px"
     :padding "1px 4px"
     :background (ui/color-primary-darkest-rgba "0.6")
     :border (str "1px solid " ui/color-primary-dark)}
    [:&:hover
     {:cursor :pointer
      :background ui/color-primary-darkest
      :border (str "1px solid " (ui/color-primary-rgba "0.7"))}]]
   [".hud-ckui-button + .hud-ckui-button"
    {:margin-left "5px"}]
   [:.hud-ckui-header-line
    {:height "1px"
     :background ui/color-primary-dark}]
   [:.hud-ckui-fill-1
    {:flex "1 1"}]
   [:.hud-ckui-fill-2
    {:flex "2 2"}]
   [:.hud-ckui-fill-3
    {:flex "3 3"}]
   [:.hud-ckui-fill-4
    {:flex "4 4"}]
   [:.hud-ckui-fill-5
    {:flex "5 5"}]
   [:.hud-ckui-pull-right
    {:text-align :right
     :padding-right "5px"}]])

(defn layout []
  [[:#hud-conky
    {:color ui/color-primary-light
     ;; \/ EXPERIMENTAL; NOT TESTED ON DEVICES *WITH* ANTI-ALIASING
     :font-smooth :never
     :-webkit-font-smoothing :none}]
   [:.hud-ck-header
    {:position :absolute
     :right "0"
     :top "0"
     :background-color (ui/color-primary-darkest-rgba "0.3")
     :display :flex
     :flex-direction :column
     :min-height (str header-size "px")
     :min-width "300px"
     :border-left (str "2px solid " ui/color-primary-light)
     :padding-top "15px"
     :z-index 999991}]
   [:.hud-ck-header-separator
    {:position :absolute
     :top (str header-size "px")
     :z-index 999990}]
   [:.hud-ck-header-separator-external
    {:height "2px"
     :width "250px"
     :right "50px"
     :background-color ui/color-primary-light}]
   [:.hud-ck-footer
    {:display :flex
     :flex-direction :column
     :min-width "300px"
     :background-color (ui/color-primary-darkest-rgba "0.3")
     :border-left (str "2px solid " ui/color-primary-light)
     :min-height (str footer-size "px")
     :position :absolute
     :bottom "0"
     :right "0"
     :z-index 999990}]
   [:.hud-ck-footer-separator
    {:position :absolute
     :right "50px"
     :bottom (str footer-size "px")
     :z-index 999990
     :width "250px"}]
   [:.hud-ck-footer-separator-internal
    {:height "1px"
     :width "298px"
     :right "0"
     :background-color ui/color-primary-darker}]
   [:.hud-ck-footer-separator-external
    {:height "2px"
     :width "250px"
     :right "50px"
     :background-color ui/color-primary-light}]
   [:.hud-ck-widget-area
    {:border-left (str "2px solid " ui/color-primary-light)
     :height (str "calc(100% - " (+ header-size footer-size) "px)")
     :display :flex
     :background-color (ui/color-primary-darkest-rgba "0.3")
     :flex-direction :column
     :position :absolute
     :top (str header-size "px")
     :overflow-x :hidden
     :overflow-y :scroll
     :font-family :profont
     :right "0"
     :padding "5px"
     :z-index 999990
     :scrollbar-width :none}]
   [".hud-ck-widget-area::-webkit-scrollbar"
    {:width "0px"}]
   [:.hud-ck-widget-area-full
    {:min-width "300px"
     :max-width "300px"}]
   [:.hud-ck-widget-area-small
    {:width "50px"}]])

(defn style []
  [[(ckui)]
   [(layout)]
   [(header)]
   [(widget-area)]
   [(footer)]
   [(widgets.style/style)]])
