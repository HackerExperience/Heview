(ns web.apps.browser.page.vpc.style
  (:require [web.ui.vars :as ui]))

(defn style []
  [[:.a-br-p-vpc-nf
    {:padding "0 10px"}]
   [:.a-br-p-vpc-nf-top
    {:border-bottom (str "1px solid " ui/color-primary)
     :padding-bottom "10px"}
    [:>h1
     {:font-size "20px"
      :font-weight :bold}]
    [:>span
     {:padding "10px 0"}]]
   [:.a-br-p-vpc-nf-bottom
    {:padding-top "8px"
     :font-style :italic}]])
