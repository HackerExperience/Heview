(ns web.view
  (:require [cljs.core.match :refer-macros [match]]
            [he.core :as he]
            [web.boot.view :as boot.view]
            [web.install.view :as install.view]
            [web.os.view :as os.view]
            [web.os.view.bsod :as os.bsod.view]
            [web.home.view :as home.view]
            [web.lock.view :as lock.view]))

(defn home-view []
  (let [has-meta-cookie? (he/subscribe [:web|meta|cookie-exists?])]
    (if has-meta-cookie?
      (lock.view/view)
      (home.view/view))))

(defn boot-view []
  (boot.view/view))

(defn play-view []
  (os.view/view))

(defn error-view []
  (os.bsod.view/view))

(defn view []
  (let [state (he/subscribe [:core|state])]
    (match state
           :home (home-view)
           :boot (boot-view)
           :play (play-view)
           :install (install.view/view)
           :else (error-view))))

