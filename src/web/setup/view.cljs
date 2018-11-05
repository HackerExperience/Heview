(ns web.setup.view
  (:require [he.core :as he]))

(defn splash-screen
  []
  [:div "Splash screen..."])

(defn error-screen
  []
  [:div "Error screen..."])

(defn view []
  (let [loading? (he/subscribe [:web|setup|loading?])
        boot-failed? (he/subscribe [:web|setup|boot-failed?])]
    [:div "Setup"
     (cond
       loading? (splash-screen)
       boot-failed? (error-screen))]))
