(ns web.home.style
  (:require [garden.core :refer [css]]
            [web.home.login.style :as login.style]))


(defn local-style []
  [])

(defn global-style []
  [[:#home
    {:height "100vh"
     :display "grid"
     :grid-template-columns "20% 30% 30% 20%"
     :grid-template-rows "[row-1-start] 10em [row-1-end row-2-start] 1fr [row-2-end row-3-start] 8rem [row-3-end]"
     :grid-template-areas "login"}]
   [:#home-login
    (login.style/local-style)]
   [(login.style/global-style)]])
