(ns web.apps.browser.page.not-found.db)

(defn build-custom
  [url]
  {:url url})

(defn build-tab
  [url]
  {:input-url url
   :base-url url
   :title "Page not found"
   :favicon "fas fa-exclamation-triangle"
   :custom (build-custom url)
   :page :not_found
   :links {}
   :path "/"
   })

