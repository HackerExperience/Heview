(ns web.apps.browser.page.not-found.handlers
  (:require [he.core :as he]
            [web.apps.browser.page.not-found.db :as not-found.db]))

(he/reg-event-fx
 :web|apps|browser|page|not-found|retry
 (fn [{gdb :db} [_ app-id tab-id url]]
   {:dispatch [:web|apps|browser|browse-url app-id tab-id url]}))
