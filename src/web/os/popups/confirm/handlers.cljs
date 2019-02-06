(ns web.os.popups.confirm.handlers
  (:require [he.core :as he]))

(he/reg-event-fx
 :web|os|popups|confirm|btn-click
 (fn [{gdb :db} [_ app-id callback]]
   {:dispatch-n
    (list [:web|wm|app|close app-id]
          callback)}))

