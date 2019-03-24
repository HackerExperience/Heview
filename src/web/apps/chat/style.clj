(ns web.apps.chat.style
  (:require [web.ui.vars :as ui]))

(defn style []
  [[:.a-ch-container
    {:display :flex
     :flex-direction :column
     :height "100%"}]
   [:.a-ch-body
    {:flex "1 1"
     :background-color (ui/color-primary-darkest-rgba "0.3")
     :padding "5px"
     :overflow-y :scroll}]
   [:.a-ch-b-message-group
    {:display :flex
     :flex-direction :row
     :margin "5px 0"
     :padding-bottom "5px"
     :border-bottom (str "1px dotted " (ui/color-primary-darker-rgba "0.6"))}]
   [:.a-ch-b-mg-avatar
    {:min-width "40px"}]
   [:.a-ch-b-mg-avatar-impl
    {:border (str "1px solid " ui/color-primary)
     :height "30px"
     :width "30px"
     :background-color "#000"
     :margin-left "5px"}]
   [:.a-ch-b-mg-main
    {:flex "1 1"
     :display :flex
     :flex-direction :column
     :padding-left "5px"}]
   [:.a-ch-b-mg-m-header
    {:display :flex
     :flex-direction :row
     :align-items :flex-start
     :padding "0 0 1px 0"}]
   [:.a-ch-b-mg-m-h-name
    {:font-weight :bold
     :font-family ui/font-monospace
     :font-size "14px"
     :flex "1 1"}]
   [:.a-ch-b-mg-m-h-timestamp
    {:align-self :flex-end
     :justify-self :flex-end
     :font-size "10px"
     :font-family :monospace
     :color ui/color-primary}]
   [:.a-ch-b-mg-m-message
    {:cursor :auto
     :-webkit-user-select :text
     :-khtml-user-select :text
     :-moz-user-select :text
     :-ms-user-select :text
     :user-select :text
     :line-height "1.35"}]
   [".a-ch-b-mg-m-message + .a-ch-b-mg-m-message"
    {:padding "2px 0"}]
   [:.a-ch-input-area
    {:min-height "50px"
     :border-top (str "1px solid " ui/color-primary)
     :padding "5px"
     :display :flex
     :flex-direction :row
     :align-items :center
     :position :relative}]
   [:.a-ch-ia-reply
    {:min-width "50px"
     :min-height "30px"
     :display :flex
     :align-items :center
     :padding "3px 5px"
     :margin "3px 5px"
     :background-color ui/color-primary-darkest
     :border (str "1px solid " ui/color-primary-dark)
     :cursor :pointer}
    [:&:hover
     {:background-color (ui/color-primary-darker-rgba "0.7")
      :border (str "1px solid " ui/color-primary)}]]
   [:.a-ch-ia-loading
    {:margin-left :auto
     :margin-right "10px"
     :font-size "20px"}]])
