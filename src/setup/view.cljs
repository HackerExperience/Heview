(ns setup.view
  (:require [he.core :as he]))

(defn splash-screen
  []
  [:div "Splash screen..."])

(defn error-screen
  []
  [:div "Error screen..."])

(defn view []
  (let [loading? (he/subscribe [:setup|loading?])
        boot-failed? (he/subscribe [:setup|boot-failed?])]
    [:div "Setup"
     (cond
       loading? (splash-screen)
       boot-failed? (error-screen))]))
