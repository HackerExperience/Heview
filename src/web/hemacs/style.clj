(ns web.hemacs.style
  (:require [web.ui.vars :as ui]))

(defn local-style []
  [[:#hemacs-minibuffer
    {:position :absolute
     :bottom "5px"
     :left "30px"
     :z-index 9999
     :display :flex
     :flex-direction :row
     :align-items :center}
    [:.hemacs-minibuffer-mode
     {:position :relative
      :max-height "20px"
      :min-width "97px"
      :padding "1px 4px"
      :color ui/color-primary-light
      :border (str "1px solid " ui/color-primary-dark)
      :background-color (ui/color-primary-darkest-rgba "0.5")}]
    [:.hemacs-minibuffer-separator
     {:height "6px"
      :width "6px"
      :display :inline-block
      :border (str "1px solid " ui/color-primary-dark)
      :background-color ui/color-primary
      :margin "0 5px"}]
    [:.hemacs-minibuffer-keybuffer
     {:min-height "20px"
      :display :flex
      :flex-direction :row
      :align-items :center}
     [:span
      {:background-color (ui/color-primary-darkest-rgba "0.5")
       :color ui/color-primary
       :border (str "1px solid " ui/color-primary-dark)
       :padding "1px 4px"
       :margin-left "2px"
       }]];
    [:.hemacs-minibuffer-output
     {:color ui/color-primary
      :border (str "1px solid " ui/color-primary-dark)
      :background-color (ui/color-primary-darkest-rgba "0.5")
      :padding "1px 4px"}
     [:.output-error
      {:color ui/color-secondary-light}]
     [:span
      {}]]]
   [:#hemacs-which-key
    {:position :absolute
     :bottom "90px"
     :left "145px"
     :background-color ui/color-background
     :color "#fff"
     :max-width "calc(100% - 400px)"
     :padding "10px"
     :border (str "1px solid " ui/color-primary-dark)
     :z-index 9999}
    [:.hemacs-which-key-grid
     {:display :flex
      :flex-wrap :wrap}
     [:.hemacs-which-key-entry
      {:flex "0 0"
       :color ui/color-primary-light
       :min-width "250px"}
      [:.entry-key
       {:font-family :monospace
        :font-weight :bold
        :font-size "16px"}]
      [:.entry-separator
       {:margin "0 4px"}]]
     [:.entry-disabled
      {:color ui/color-primary-dark}]]]])

(defn global-style []
  [[:.hemacs-marker
    {:position :absolute
     :margin "-7px 0 0 -7px"
     :width "18px"
     :height "18px"
     :border (str "1px solid " ui/color-background)
     :background-color ui/color-secondary-light
     :color ui/color-background
     :display :flex
     :justify-content :center}
    [:span
     {:font-weight :bold}]]])
