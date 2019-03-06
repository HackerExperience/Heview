(ns web.apps.browser.page.not-found.style
  (:require [web.ui.vars :as ui]))

(defn local-style []
  [[:.br-page-not-found
    {:display :flex
     :flex-direction :row
     :margin-top "10%"
     :margin-left "5%"
     :width "360px"}
    [:.br-p-nf-icon
     {:min-width "50px"
      :font-size "20px"
      :padding-left "10px"
      :padding-top "5px"}]
    [:.br-p-nf-info
     {:flex "1 1"
      :display :flex
      :flex-direction :column}]
    [:.br-p-nf-info-title
     {:font-size "26px"
      :padding-bottom "12px"
      :border-bottom (str "1px solid " ui/color-primary)}]
    [:.br-p-nf-info-text
     {:padding-top "10px"}
     [:ul
      {:margin "0"
       :padding-top "10px"
       :padding-left "16px"}]
     [:li
      {:padding-bottom "5px"}]]
    [:.br-p-nf-info-retry
     {:padding-top "15px"}
     [:button
      {:height "24px"
       :width "100px"}]]]])
