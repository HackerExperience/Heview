(ns web.os.header.view
  (:require [he.core :as he]))

(defn logout
  []
  [:button {:on-click #(he/dispatch [:game|logout])} "Logout"])

(defn view-header-left
  []
  [:div#os-header-left])

(defn view-header-center
  []
  [:div#os-header-center])

(defn view-header-right
  []
  [:div#os-header-right
   [logout]])

(defn view
  []
  [:div#os-header
   [view-header-left]
   [view-header-center]
   [view-header-right]])
