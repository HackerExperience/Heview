(ns web.view
  (:require [cljs.core.match :refer-macros [match]]
            [he.core :as he]
            [boot.view]
            [web.os.view :as os.view]
            [web.home.view :as home.view]))

(defn home-view []
  (home.view/view))

(defn boot-view []
  (boot.view/view))

(defn play-view []
  (os.view/view))

(defn error-view []
  [:div "Error"])

(defn view []
  (let [state (he/subscribe [:core|state])]
    (match state
           :home (home-view)
           :boot (boot-view)
           :play (play-view)
           :else (error-view))))

