(ns core.view
  (:require [cljs.core.match :refer-macros [match]]
            [he.core :as he]
            [setup.view]
            [os.view]
            [home.view]))

(defn home-view []
  (home.view/view))

(defn setup-view []
  (setup.view/view))

(defn play-view []
  (os.view/view))

(defn error-view []
  [:div "Error"])

(defn view []
  (let [state (he/subscribe [:core|state])]
    (match state
           :home (home-view)
           :setup (setup-view)
           :play (play-view)
           :else (error-view))))

