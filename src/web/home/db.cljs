(ns web.home.db
  (:require [web.home.login.db :as login.db]))

(def initial
  {:login login.db/initial})

