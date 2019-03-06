(ns web.apps.browser.page.vpc.db
  (:require [web.apps.browser.page.utils :as page.utils]))

(defn build-tab
  [data opts]
  (println data)
  (let [[_ ip] (get-in data [:meta :nip])
        url (page.utils/parse ip opts)]
    {:input-url (:input-url url)
     :base-url (:base-url url)
     :title ip
     :favicon "fa fa-user"
     :custom {}
     :links {}
     :page :vpc
     :path (:path url)}))
