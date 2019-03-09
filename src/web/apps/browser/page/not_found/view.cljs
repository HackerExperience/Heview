(ns web.apps.browser.page.not-found.view
  (:require [he.core :as he]))

(defn on-retry-click
  [app-id tab-id url _]
  (he/dispatch [:web|apps|browser|page|not-found|retry app-id tab-id url]))

(defn view
  [app-id tab-id _]
  (let [url (he/subscribe [:web|apps|browser|page|not-found|url app-id])]
    [:div.a-br-page-not-found
     [:div.a-br-p-nf-icon
      [:i.fa.fa-info-circle]]
     [:div.a-br-p-nf-info
      [:div.a-br-p-nf-info-title
       [:span "Server not found"]]
      [:div.a-br-p-nf-info-text
       [:span (str "Icefox can't find the server at " url ".")]
       [:ul
        [:li
         [:span "Check the address for typing errors such as "]
         [:b "ww"]
         [:span ".example.com instead of "]
         [:b "www"]
         [:span ".example.com"]]
        [:li
         [:span (str "If you are unable to load any pages, check your "
                     "computer's network connection.")]]
        [:li
         [:span (str "If your computer or network is protected by a firewall "
                     "or proxy, make sure that Icefox is permitted to access "
                     "the Web.")]]]]
      [:div.a-br-p-nf-info-retry
       {:on-click #(on-retry-click app-id tab-id url %)}
       [:button.ui-btn
        [:span "Try again"]]]]]))
