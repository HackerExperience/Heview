(ns web.install.style
  (:require [web.ui.vars :as ui]))

(defn steps []
  [[:.inst-step
    {:padding "50px"
     :font-size "13px"}]
   [:.inst-s-input-area
    {:position :relative}]
   [:.inst-s-input
    {:width "100%"
     :min-height "20px"
     :padding "0 35px 0 3px"
     :border-top "2px ridge #656563"
     :border-left "2px ridge #656563"
     :border-right "1px ridge #b0b0b0"
     :border-bottom "1px ridge #b0b0b0"}]
   [:.inst-s-input-error
    {:border-top "2px ridge #ff0000"
     :border-left "2px ridge #ff0000"
     :border-right "1px ridge #ff0000"
     :border-bottom "1px ridge #ff0000"}]
   [:.inst-s-input-badge
    {:position :absolute
     :right "5px"
     :top "4px"}]
   [:.inst-s-input-badge-ok
    {:color "#006400"}]
   [:.inst-s-input-badge-error
    {:color "#ff0000"}]
   [:.inst-s-input-eye
    {:position :absolute
     :right "5px"
     :top "4px"
     :cursor :pointer}]
   [".inst-s-input-badge + .inst-s-input-eye"
    {:right "23px"}]
   [:.inst-step-register
    {:display :flex
     :flex-direction :row}]
   [:.inst-s-r-icon
    {:min-width "60px"
     :font-size "40px"
     :color "#333"}]
   [:.inst-s-r-main
    {:flex "1 1"
     :display :flex
     :flex-direction :column}]
   [:.inst-s-r-m-title
    {:min-height "50px"
     :font-size "12px"}]
   [:.inst-s-r-m-form
    {:flex "1 1"
     :display :flex
     :flex-direction :column
     :position :relative
     :font-size "12px"}]
   [:.inst-s-r-m-f-entry
    {:display :flex
     :flex-direction :row}]
   [".inst-s-r-m-f-entry + .inst-s-r-m-f-entry"
    {:padding-top "10px"}]
   [:.inst-s-r-m-f-e-label
    {:min-width "200px"}]
   [:.inst-s-r-m-f-e-input
    {:flex "1 1"}]
   [:.inst-s-r-m-f-errors
    {:display :flex
     :flex-direction :column
     :position :absolute
     :top "95px"
     :left "60px"}]
   [:.inst-s-r-m-f-error
    {:display :flex
     :flex-direction :row
     :align-items :center}
    [:>i
     {:color "#ff0000"}]
    [:>span
     {:padding-left "10px"}]]
   [".inst-s-r-m-f-error + .inst-s-r-m-f-error"
    {:padding-top "5px"}]
   [:.inst-s-v-m-result
    {:margin-left :auto
     :margin-right :auto
     :padding-top "20px"
     :font-size "30px"
     :display :flex
     :flex-direction :column
     :min-height "30px"
     :min-width "30px"}]
   [:.inst-s-v-m-result-error
    {:color "#ff0000"
     :margin-left :auto
     :margin-right :auto}]
   [:.inst-s-v-m-result-success
    {:color "#006400"}]
   [:.inst-s-v-m-result-msg
    {:font-size "12px"}]
   [:.inst-step-document
    {:display :flex
     :flex-direction :column
     :min-height "100%"
     :padding "15px 50px"}]
   [:.inst-s-d-title
    {:min-height "30px"}]
   [:.inst-s-d-textarea
    {:margin "5px 0"
     :flex "1 1"
     :border-top "2px ridge #656563"
     :border-left "2px ridge #656563"
     :border-right "1px ridge #b0b0b0"
     :border-bottom "1px ridge #b0b0b0"
     :background-color "#fff"
     :overflow-y :scroll
     :overflow-x :hidden
     :max-height "190px"
     :cursor :auto
     :-webkit-user-select :text
     :-khtml-user-select :text
     :-moz-user-select :text
     :-ms-user-select :text
     :user-select :text
     :padding "5px"
     :scrollbar-color "#bbb #e3e3e3"
     :scrollbar-width :auto}]
   [".inst-s-d-textarea::-webkit-scrollbar"
    {:width "15px"}]
   [".inst-s-d-textarea::-webkit-scrollbar-track"
    {:background "#e3e3e3"
     :border "1px solid #ccc"}]
   [".inst-s-d-textarea::-webkit-scrollbar-thumb"
    {:background "#d1d1d1"
     :border "1px solid #979797"}]
   [".inst-s-d-textarea::-webkit-scrollbar-thumb:active"
    {:background "#ccc"
     :border "1px solid #666"}]
   [:.inst-s-d-radio
    {:min-height "40px"
     :padding-top "5px"}]
   [:.inst-s-d-radio-entry
    {:display :flex
     :align-items :center}
    [:>span
     {:padding-left "5px"}]]
   [".inst-s-d-radio-entry + .inst-s-d-radio-entry"
    {:padding-top "3px"}]
   [:.inst-step-pricing
    {:padding "20px 30px"
     :display :flex
     :flex-direction :column
     :min-height "100%"}]
   [:.inst-s-p-title
    {:padding-left "145px"}]
   [:.inst-s-p-table
    {:display :flex
     :flex-direction :row
     :flex "1 1"
     :padding-top "15px"}]
   [:.inst-s-p-table-col
    {:flex "1 1"
     :display :flex
     :flex-direction :column
     :align-items :center
     :margin "0 5px"
     :max-height "238px"}]
   [:.inst-s-p-table-col-desc
    {:min-width "135px"}]
   [:.inst-s-p-table-col-plan
    {:background "#ddd"
     :border "1px solid #b0b0b0"}
    [:&:hover
     {:background "#eee"
      :cursor :pointer}]]
   [:.inst-s-p-table-col-plan-selected
    {:border "1px solid #0645ad"
     :background "#f7f7f7"}
    [:&:hover
     {:background "#fff"}]]
   [:.inst-s-p-table-row-name
    {:min-height "20px"
     :font-size "15px"
     :font-weight :bold
     :padding-top "2px 0"
     :width "100%"
     :display :flex
     :align-items :center
     :justify-content :center
     :background "#eee"
     :border-bottom "1px solid #ccc"}]
   [:.inst-s-p-table-row-price
    {:font-size "20px"
     :font-family :monospace
     :min-height "25px"
     :display :flex
     :align-items :flex-end}]
   [:.inst-s-p-table-row-frequency
    {:font-size "11px"
     :min-height "20px"}]
   [:.inst-s-p-table-row-billed
    {:font-size "11px"
     :min-height "15px"}]
   [:.inst-s-p-table-row-separator
    {:width "100%"
     :margin-top "5px"
     :border-top "1px solid #b0b0b0"}]
   [:.inst-s-p-table-row-logo
    {:min-height "87px"}]
   [:.inst-s-p-table-row-item
    {:font-size "12px"
     :min-height "30px"
     :display :flex
     :align-items :center
     :justify-content :center
     :width "100%"
     :border-bottom "1px solid #ccc"}]
   [:.inst-s-p-table-row-item-desc
    {:text-align :right
     :align-self :flex-end
     :border-bottom :none
     :justify-content :flex-end
     :position :relative}]
   [:.inst-s-p-table-row-item-help
    {:position :absolute
     :left "0"
     :color "#0645ad"
     :font-size "12px"
     :cursor :pointer
     :padding "5px"}]
   [:.inst-s-p-table-row-item-help-tooltip
    {:position :absolute
     :left "15px"
     :top "30px"
     :min-width "150px"
     :background "#eee"
     :padding "5px"
     :z-index "999"
     :text-align :left
     :border "1px solid #aaa"
     :display :none}]
   [:.inst-s-p-table-row-item-help-tooltip-visible
    {:display :block}]
   [:.inst-s-p-table-row-item-ok
    {:color "#006400"}]
   [:.inst-s-p-table-row-item-nope
    {:color "#ff0000"}]
   [:.inst-step-finish
    {:display :flex
     :flex-direction :row}]
   [:.inst-s-f-main
    {:flex "1 1"
     :display :flex
     :flex-direction :column}]
   [:.inst-s-f-m-title
    {:font-size "20px"
     :margin-bottom "10px"}]
   [:inst-s-f-m-text-entry
    {:font-size "16px"}]])

(defn window []
  [[:.inst-m-b-window
    {:display :flex
     :flex-direction :column
     :background-color "#d0d0d0"
     :align-self :center
     :width "600px"
     :height "430px"
     :margin-top :auto
     :margin-bottom :auto
     :padding "2px 3px 3px 2px"
     :border-top (str "2px ridge #cadaf4");
     :border-right (str "1px ridge #414a51")
     :border-bottom (str "1px ridge #414a51")
     :border-left (str "2px ridge #cadaf4")}]
   [:.inst-m-b-w-header
    {:min-height "20px"
     :font-size "12px"
     :color "#fff"
     :background-color "#0f24cd"
     :display :flex
     :align-items :center
     :font-weight :bold
     :padding-left "10px"}]
   [:.inst-m-b-w-subheader
    {:min-height "60px"
     :position :relative
     :background-color "#fff"
     :color "#000"
     :font-size "12px"
     :padding "5px 10px"
     :display :flex
     :flex-direction :row}]
   [:.inst-m-b-w-sh-description
    {:display :flex
     :flex-direction :column
     :max-width "91%"}]
   [:.inst-m-b-w-sh-desc-title
    {:font-weight :bold
     :padding-left "20px"}]
   [:.inst-m-b-w-sh-desc-text
    {:padding-left "50px"
     :padding-top "5px"}]
   [:.inst-m-b-w-sh-icon
    {:position :absolute
     :right "5px"
     :display :flex
     :font-size "40px"
     :background-color "#0e0c5f"
     :color "#fff"
     :padding "5px"}]
   [:.inst-m-b-w-subheader-separator
    {:width "99%"
     :height "2px"
     :margin-left :auto
     :margin-right :auto
     :background-color "#969696"}]
   [:.inst-m-b-w-content
    {:flex "1 1"}]
   [:.inst-m-b-w-c-link
    {:color "#0645ad"}
    [:&:hover
     {:color "#0b0080"
      :cursor :pointer}]]
   [:.inst-m-b-w-footer-separator
    {:width "99%"
     :height "2px"
     :margin-left :auto
     :margin-right :auto
     :background-color "#909090"}]
   [:.inst-m-b-w-footer
    {:min-height "40px"
     :position :relative
     :display :flex
     :flex-direction :row
     :align-items :center}]
   [:.inst-m-b-w-f-extra-button
    {:position :absolute
     :left "10px"
     :font-size "12px"}]
   [:.inst-m-b-w-f-buttons
    {:position :absolute
     :right "145px"
     :font-size "12px"}]
   [:.inst-m-b-w-f-button
    {:min-width "80px"
     :min-height "20px"
     :margin-left "10px"
     :padding "2px 5px"
     :border-top "1px ridge #898987"
     :border-left "1px ridge #898987"
     :border-bottom "2px ridge #3e3e3e"
     :border-right "2px ridge #3e3e3e"
     :cursor :pointer}]
   [:.inst-m-b-w-f-button-disabled
    {:color "#848484"
     :cursor :auto}]
   [:.inst-m-b-w-f-loading
    {:position :absolute
     :display :flex
     :align-items :center
     :right "15px"
     :font-size "20px"}]])

(defn welcome []
  [[:.inst-m-b-welcome
    {:display :flex
     :flex-direction :column
     :margin-left :auto
     :margin-right :auto
     :margin-top "150px"
     :color "#ddd"
     :font-weight :bold
     :text-shadow "1px 1px #000"}]
   [:.inst-m-b-welcome-title
    {:display :flex
     :flex-direction :row
     :align-items :center
     :margin-left "-120px"
     :min-height "40px"}]
   [:.inst-m-b-welcome-title-icon
    {:font-size "40px"
     :min-width "50px"}]
   [:.inst-m-b-welcome-title-text
    {:font-size "26px"
     :padding-left "20px"}]
   [:.inst-m-b-welcome-area
    {:flex "1 1"
     :display :flex
     :flex-direction :column}]
   [:.inst-m-b-welcome-subtitle
    {:min-height "30px"
     :margin-top "20px"
     :margin-left "-40px"
     :margin-bottom "10px"
     :display :flex
     :flex-direction :row
     :align-items :center}]
   [:.inst-m-b-welcome-subtitle-icon
    {:font-size "25px"
     :min-width "20px"}]
   [:.inst-m-b-welcome-subtitle-text
    {:padding-left "20px"
     :font-size "20px"}]
   [:.inst-m-b-welcome-entry
    {:display :flex
     :flex-direction :row
     :align-items :center
     :padding-top "10px"
     :min-height "26px"}
    [:&:hover
     {:color "#fff"
      :cursor :pointer}
     [:>.inst-m-b-welcome-entry-text
      {:text-decoration :underline
       :text-decoration-color "#fff"}]]]
   [:.inst-m-b-welcome-entry-icon
    {:font-size "26px"
     :min-width "36px"
     :color "#0b6af3"
     :text-shadow "0 0 1px #fff"}]
   [:.inst-m-b-welcome-entry-icon-main
    {:color "#74c872"}]
   [:.inst-m-b-welcome-entry-text
    {:font-size "18px"}]
   ])

(defn error-window []
  [[:.inst-error-window
    {:position :absolute
     :left "450px"
     :right 0
     :top 0
     :bottom 0
     :margin :auto
     :z-index 999
     :width "470px"
     :height "150px"}]])

(defn sidebar []
  [[:.inst-m-sb-steps
    {:display :flex
     :flex-direction :column}]
   [:.inst-m-sb-step
    {:display :flex
     :flex-direction :row}]
   [:.inst-m-sb-step-icon
    {:background-color "#fff"
     :width "15px"
     :height "15px"
     :border-radius "50%"
     :border "2px solid #173f85"
     :margin-top :auto
     :margin-bottom :auto}]
   [:.inst-m-sb-step-name
    {:padding-left "20px"}]
   [:.inst-m-sb-step-current
    [:>.inst-m-sb-step-icon
     {:box-shadow "inset 0 0 2px 2px rgba(223,135,56,1)"}]
    [:>.inst-m-sb-step-name
     {:color "#ff8400"
      :font-weight :bold}]]
   [:.inst-m-sb-step-completed
    [:>.inst-m-sb-step-icon
     {:box-shadow (str "inset 0 0 2px 2px rgba(255,255,255,1), "
                       "inset 0 0 6px 6px rgba(27,221,26,1)")}]]
   [".inst-m-sb-step + .inst-m-sb-step"
    {:padding-top "10px"}]
   [:.inst-m-sb-progress
    {:padding-top "25px"
     :font-weight :bold
     :display :flex
     :flex-direction :column}]
   [:.inst-m-sb-progress-desc]
   [:.inst-m-sb-progress-time
    {:padding-left "20px"}]])

(defn layout []
  [[:.inst
    {:height "100vh"
     :display :flex
     :flex-direction :column
     :font-family ui/font-family}]
   [:.inst-header
    {:min-height "60px"
     :background-color "#143376"}]
   [:.inst-header-separator
    {:height "4px"
     :width "100%"
     :background (str "linear-gradient(to left, "
                      "#6178ae 0%,"
                      "#fff 35%, "
                      "#fff 55%, "
                      "#6178ae 100%)")}]
   [:.inst-main
    {:flex "1 1"
     :display :flex
     :flex-direction :row}]
   [:.inst-main-sidebar
    {:background-color "#5f82d3"
     :color "#fff"
     :min-width "430px"
     :display :flex
     :flex-direction :column
     :padding-left "40px"
     :padding-top "50px"
     :cursor :default}]
   [:.inst-main-body
    {:flex "1 1"
     :background-color "#5577d0"
     :display :flex
     :flex-direction :column}]
   [:.inst-footer
    {:min-height "60px"
     :background-color "#143376"}]
   [:.inst-footer-separator
    {:height "4px"
     :width "100%"
     :background (str "linear-gradient(to left, "
                      "#6178ae 0%,"
                      "#e26b29 35%, "
                      "#e26b29 55%, "
                      "#6178ae 100%)")}]])

(defn style []
  [[(layout)]
   [(sidebar)]
   [(error-window)]
   [(window)]
   [(steps)]
   [(welcome)]])
