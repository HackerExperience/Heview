(ns boot.view
  (:require [he.core :as he]))

(defn splash-screen
  []
  [:div "Splash screen..."])

(defn error-screen
  []
  [:div "Error screen..."])

(defn view []
  (let [loading? (he/subscribe [:boot|loading?])
        boot-failed? (he/subscribe [:boot|failed?])]
    [:div "Boot"
     (cond
       loading? (splash-screen)
       boot-failed? (error-screen))]))
