(ns web.apps.browser.page.download-center.db
  (:require [web.apps.browser.page.utils :as page.utils]))

(defn build-tab
  [data opts]
  (let [base-url "dc.com"
        url (page.utils/parse base-url opts)]
    {:input-url (:input-url url)
     :base-url (:base-url url)
     :title "Download Center"
     :favicon "fas fa-download"
     :custom {}
     :links {"/" {:text "Home"
                  :icon "fa fa-archive"
                  :order 0}
             "/public" {:text "Public Files"
                        :icon "fas fa-download"
                        :order 1}
             "/about" {:text "About us"
                       :icon "fa fa-user"
                       :order 2}}
     :page :download-center
     :path (:path url)}))
