(ns web.ui.style
  (:require [garden.core :refer [css]]
            [web.ui.vars :as ui]))

(defn local-style []
  {})

(defn global-style []
  [
   ;; Buttons
   [:.ui-btn-area-large {}
    ["button + button"
     {:margin-left "15px"}]]
   [:.ui-btn-area {}
    ["button + button"
     {:margin-left "10px"}]]
   [:.ui-btn
    {:height "20px"
     :background-color (ui/color-primary-darker-rgba "0.5")
     :border (str "1px solid" ui/color-primary-light)
     :color ui/color-primary-light
     :cursor :pointer}
    [[:&:hover
      {:background-color (ui/color-primary-rgba "0.35")
       :border (str "1px solid" ui/color-primary-lightest)
       :color ui/color-primary-lightest}]]]
   [:.ui-btn-disabled
    {:color ui/color-primary
     :border (str "1px solid " ui/color-primary)
     :background-color (ui/color-primary-darker-rgba "0.35")
     :cursor :initial}
    [:&:hover
     {:color ui/color-primary
      :border (str "1px solid " ui/color-primary)
      :background-color (ui/color-primary-darker-rgba "0.35")}]]
   [:.btn-icon
    {:height "26px"
     :width "40px"
     :padding "0"}
    [:i
     {:font-size "16px"}]]
   [:.btn-dual
    {:width "75px"
     :height "26px"
     :position :relative}
    [:i
     {:opacity 0
      :transition "opacity 0.1s"
      :position :absolute
      :top "50%"
      :left "50%"
      :margin-left "-7px"
      :margin-top "-6px"}]
    [:span
     {:transition "opacity 0.25s"}]
    [:&:hover {}
     [:i
      {:margin-top "-8px"
       :font-size "16px"
       :opacity 1
       :transition "opacity 0.25s"}]
     [:span
      {:opacity 0
       :transition "opacity 0.1s"}]]]
   [:.btn-primary
    {:background-color (ui/color-primary-light-rgba "0.45")
     :border (str "1px solid" ui/color-primary-lightest)
     :text-shadow (str "0 0 " ui/color-primary)
     :color ui/color-background}
    [:&:hover
     {:background-color ui/color-primary-light
      :border (str "1px solid" ui/color-primary-lightest)
      :color ui/color-primary-darker}]]
   [:.ui-tbl-row
    {:line-height "24px"
     :border "1px solid #eee"}]

   ;; Spinner
   [:.ui-spinner-area
    {:cursor :wait}]

   ;; Input
   [:.ui-input
    {:border (str "1px solid" ui/color-primary-light)
     :background ui/color-primary-darkest
     :color ui/color-primary-light
     :height "24px"
     :font-size "12px"
     :padding "4px 22px 4px 4px"
     :width "150px"
     :text-decoration :none}
    [:&:hover
     {:background (ui/color-primary-darker-rgba "0.4")
      :border (str "1px solid" ui/color-primary-lighter)}]
    [:&:focus
     {:background (ui/color-primary-darker-rgba "0.75")
      :border (str "1px solid" ui/color-primary-lightest)}]]

   ;; Scroll
   ;; Scroll > Firefox
   ["*"
    {:scrollbar-color (str ui/color-primary ui/color-primary-darker)
     :scrollbar-width :thin}]
   ;; Scroll > Chrome
   ["::-webkit-scrollbar"
    {:width "6px"}]
   ["::-webkit-scrollbar-track"
    {:background ui/color-primary-darker
     :border (str "1px solid" ui/color-primary)}]
   ["::-webkit-scrollbar-thumb"
    {:background ui/color-primary
     :border (str "1px solid " ui/color-primary-light)}]
   ["::-webkit-scrollbar-thumb:active"
    {:background (ui/color-primary-light-rgba "0.525")
     :border (str "1px solid " ui/color-primary-lightest)}]

   ;; Help
   [:.ui-help
    {:display :block
     :margin "0 5px"
     :padding "3px 2px"
     :font-size "12px"}
    [:i
     {:color ui/color-primary-dark
      :margin-top "2px"}
     [:&:hover
      {:color ui/color-primary}]]]

   ;; Tooltip
   ["[tip]"
    {:position :relative
     :display :inline-block}]
   ["[tip]:after"
    {:content "attr(tip)"
     :position :absolute
     :left "50%"
     :padding "5px"
     :background ui/color-background
     :border (str "1px solid" ui/color-primary-light)
     :color ui/color-primary-light
     :text-align :center
     :min-width "80px"
     :white-space :nowrap
     :pointer-events :none
     :z-index 99999
     :opacity 0
     :font-size "12px"
     :font-family ui/font-family
     :font-weight :normal

     ;; Fix "blurriness" on chrome
     "-webkit-backface-visibility" "hidden"

     :box-shadow (str
                  "0 0 7px 3px rgba(0,0,0,0.8)"
                  ","
                  "0 0 10px 6px rgba(0,0,0,0.4)")
     ;; Bottom
     :top "100%"
     :margin-top "9px"
     :transform "translateX(-50%) translateY(0%)"
     }]
   ["[tip]:hover::after"
    {:opacity 1
     :transition "opacity 0.5s ease-out"}]

   ;; Re-com customization

   [:.chosen-container-single {}
    [:.chosen-single
     {:background-color ui/color-primary-darkest
      :border (str "1px solid " ui/color-primary)
      :border-radius "0px"
      :color ui/color-primary-light}
     [:&:hover
      {:background (ui/color-primary-dark-rgba "0.5")
       :border-color (ui/color-primary-light-rgba "0.45")}]
     [:div {}
      [:b
       {:background "url(\" data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAANCAYAAABy6+R8AAAA5UlEQVQokZ3SoU5DAQyF4a83yzKxEASCoBeCAIcYBMHue+AWHAaL2CNg4AWGIgg84vIWYFAI9IJaCFkRXALbIGSrbf/29LSR0qJRLEygAZHlKtYyqqfZgpiUTWFT5kMW9xMoIssNXOIqsjycArJsCH3cijiNSdn6kreOXXRr8KCe0EIf5+hgT1iBkL0C27jGFp4xwA6O0cQdjjKq0aeC2r3Islt37WKEdq1kiLOM6uVb9g/L6/1usI8xhtJJFtXb9K4zd4rsdYgBHnGRUb3OOfrbcSPLNsYZ1ftc8i/ov1jqI5aCPgDF/0aCuqfn6AAAAABJRU5ErkJggg== \") no-repeat 0 10px"}]]]
    [:.chosen-search
     ["input[type=\"text\"]"
      {:background "url(\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA8AAAAPCAYAAAA71pVKAAABUElEQVQokZXSPWtUARCF4WcuKcRKgoRFUiwiQYJFKrGw8VYWWsZ0io291eIvEKu0kn8gVnaicO3EwkJkCUFELCyWZUkpYnHHIpPAXRbRA6ca3vk4TKR0qujbNWFb2hB6mTPiKJuut0JxCke2l2Q+FnEHG+gxwws8z+gWK+HIdhNvcRWH+IYGW7iCD7i73KCJbM9JkwLfYHfJH3EDjyLbZjBZ3rqOlzjGbkb3dbhau1P1XzL3snl3eDYZo/IU31fk8qU8EjEarF2GXlqVal+1ZrnQYI5FhbO5Ar5cXpTPtIZPToK6LzyLvp3gh9BgjH1sCwc4GsAZ3c/I9ilu454wrhsbXMMOSOvChdoUwyfZwqSaXKz6DO/rnJt4jQcZ3XwAV4PzNWmjQpoLnwt+5eQXDvAkozsewH9TbbZfjR9mdNN/hiH6diysY5rR/f4veFl/AM/Be4G5B1vHAAAAAElFTkSuQmCC \") no-repeat 98% 5px"
       :background-color (ui/color-primary-dark-rgba "0.3")
       :border-radius "0px"
       :border (str "1px solid " ui/color-primary)
       :color ui/color-primary-light}]]]
   [:.chosen-container
    [:.chosen-drop
     {:background ui/color-primary-darkest
      :border-radius "0px"
      :border (str "1px solid " ui/color-primary-light)}]
    [:.chosen-results
     [:li
      {:color ui/color-primary-light
       :border (str "1px solid " ui/color-primary-darkest)}
      [:&:hover
       {:background (ui/color-primary-dark-rgba "0.3")
        :border (str "1px solid " (ui/color-primary-rgba "0.8"))}]
      [:&.group-result
       {:color ui/color-primary-lightest
        :border :none}
       [:&:hover
        {:background ui/color-primary-darkest
         :border :none}]]
      [:&.highlighted
       {:background (ui/color-primary-dark-rgba "0.6")
        :color ui/color-primary-light}
       [:&:hover
        {:background (ui/color-primary-dark-rgba "0.7")}]]]
     [:.no-results
      {:background (ui/color-primary-darkest-rgba "1.0")
       :color ui/color-primary-light}]]]
   [:.chosen-container-active
    [:&.chosen-with-drop
     [:.chosen-single
      {:border (str "1px solid " ui/color-primary-lightest)
       :background "url(\" data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAANCAYAAABy6+R8AAAA3klEQVQoka3SMUpDYRAE4G8fQYJYhpRWVhLBK/jjGewEwdpYWVhb2JvOC6RIZy3PO1iIFl5ALCwkRZCsha9Q8oIGnGphZ2Z3h42UVkW1smKZKLKsRZalhguNyLIpjTCMLBvtpt9uiiw9jFEwla6Fs4x63jopsuxign1MsS4MMYos/R+T5F4lbQkTDPCCC+zgqOHd4DijfodOQxxjuxEcokYXz7jEAT4iy2lG/drBm/CImXSeVX3buE8jy1VTn+CpWfsriJiXvtDLqB9a0uxiIPM+q7vZQnp/xf99xG/4BNuoRty1Zw3/AAAAAElFTkSuQmCC\") no-repeat 98% 10px"
       :background-color (ui/color-primary-dark-rgba "0.8")
       :color ui/color-primary-lighter
       :box-shadow :none}]]
    [:.chosen-choices
     {:border (str "1px solid " ui/color-primary-lightest)
      :box-shadow :none}]]])
