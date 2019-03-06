(ns web.apps.browser.page.vpc.view
  (:require [he.core :as he]))

(defn view-not-found
  [app-id]
  (let [ip (he/subscribe [:web|apps|browser|tab|base-url app-id])
        path (he/subscribe [:web|apps|browser|tab|path app-id])
        path-str (if (= path "/")
                   "/index.html"
                   (str path ".html"))]
    [:div.br-p-vpc-nf
     [:div.br-p-vpc-nf-top
      [:h1 "Not Found"]
      [:span (str "The requested URL " path-str " not found on this server.")]]
     [:div.br-p-vpc-nf-bottom
      [:span (str "Apache/2.4.38 (Debian) Server at " ip " Port 80")]]]))

(defn view
  [app-id tab-id link-info]
  [:div.br-page-vpc
   [view-not-found app-id]])
