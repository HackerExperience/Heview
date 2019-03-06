(ns web.apps.browser.page.style
  (:require [web.ui.vars :as ui]
            [web.apps.browser.page.download-center.style :as dc]
            [web.apps.browser.page.not-found.style :as nf]
            [web.apps.browser.page.vpc.style :as vpc]))

(defn components []
  [[:.br-pc-a
    {:color ui/color-secondary}
    [:&:hover
     {:cursor :pointer
      :color ui/color-secondary-light}]]
   [:.br-pc-nf
   {:display :flex
    :flex-direction :column
    :align-items :center
    :padding "0 10px"}
   [:.br-pc-nf-top
    {:border-bottom (str "1px solid " ui/color-primary)
     :width "100%"
     :display :flex
     :justify-content :center
     :font-weight :bold}
    [:h1
     {:font-size "20px"}]]
   [:.br-pc-nf-bottom
    {:padding-top "8px"}]]])

(defn local-style []
  [[(components)
    (dc/local-style)
    (nf/local-style)
    (vpc/local-style)]])
