(ns web.apps.browser.page.db
  (:require [cljs.core.match :refer-macros [match]]
            [web.apps.browser.page.download-center.db :as download-center.db]
            [web.apps.browser.page.home.db :as home.db]
            [web.apps.browser.page.not-found.db :as not-found.db]
            [web.apps.browser.page.vpc.db :as vpc.db]))

(defn keywordize-tab-type
  [type]
  (match type
         "npc_download_center" :download-center
         "npc_bank" :bank
         "vpc" :vpc
         else (he.error/match "Unknown tab keywordize type" else)))

(defn build-tab
  [args opts]
  (let [identifier (if (keyword? args) [args] args)]
    (match identifier
           [:home] (home.db/build-tab)
           [:not-found url] (not-found.db/build-tab url)
           [:download-center data] (download-center.db/build-tab data opts)
           [:vpc data] (vpc.db/build-tab data opts)
           else (he.error/match "Unknown tab" else))))
