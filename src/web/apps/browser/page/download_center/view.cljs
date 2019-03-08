(ns web.apps.browser.page.download-center.view
  (:require [cljs.core.match :refer-macros [match]]
            [he.core :as he]
            [web.apps.browser.page.components :as page.components]))

(defn view-index
  [app-id tab-id]
  [:div
   [page.components/link app-id tab-id "/about" "About us"]])

(defn view-public
  [app-id tab-id]
  [:div "Public"])

(defn view-about
  [app-id tab-id]
  [:div
   [page.components/link app-id tab-id "/" "Go back"]])

(defn render-body
  [app-id tab-id path]
  (match path
         "/" [view-index app-id tab-id]
         "/public" [view-public app-id tab-id]
         "/about" [view-about app-id tab-id]
         else [page.components/not-found app-id tab-id path]))

(defn view
  [app-id tab-id [links path]]
  [:div.br-page-download-center
   [:div.br-page-dc-header
    [page.components/tab app-id tab-id links path]]
   [:div.br-page-dc-body
    [render-body app-id tab-id path]]])
