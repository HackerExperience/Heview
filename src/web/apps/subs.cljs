(ns web.apps.subs
  (:require [re-frame.core :as rf]
            [web.apps.log-viewer.subs]))

(defn apps
  [db _]
  (get-in db [:wm :apps]))

