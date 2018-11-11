(ns web.apps.view
  (:require [he.core :as he]
            [web.apps.dispatcher :as apps.dispatcher]))

(defn view
  [app-id]
  (let [app-type (he/subscribe [:web|apps|type app-id])]
    [apps.dispatcher/dispatch-view app-type app-id]))

