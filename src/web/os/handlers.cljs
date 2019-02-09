(ns web.os.handlers
  (:require [he.core :as he]
            [web.os.db :as os.db]
            [web.os.header.handlers]
            [web.os.popups.handlers]))

(he/reg-event-fx
 :web|os|error|runtime
 (fn [_ [_ error-data]]
   {:dispatch [:web|wm|app|open-popup
               [:os :os-error nil] [error-data] []]}))
