(ns web.apps.browser.page.view
  (:require [cljs.core.match :refer-macros [match]]
            [web.apps.browser.page.download-center.view :as download-center]
            [web.apps.browser.page.home.view :as home]
            [web.apps.browser.page.not-found.view :as not-found]
            [web.apps.browser.page.vpc.view :as vpc]))

(defn render-page
  [app-id tab-id link-info page]
  (match page
         :home (home/view app-id tab-id link-info)
         :not_found (not-found/view app-id tab-id link-info)
         :download-center (download-center/view app-id tab-id link-info)
         :vpc (vpc/view app-id tab-id link-info)
         else (he.error/match "Unknown page" else)))
