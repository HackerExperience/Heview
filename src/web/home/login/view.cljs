(ns web.home.login.view
  (:require [he.core :as he]))

(defn form-input-username
  []
  [:input
   {:placeholder "Username"
    :value (he/subscribe [:web|home|login|form|username])
    :on-change #(he/dispatch [:web|home|login|form|change-username
                              (-> % .-target .-value)])}])

(defn form-input-password
  []
  [:input
   {:type "password"
    :placeholder "Password"
    :value (he/subscribe [:web|home|login|form|password])
    :on-change #(he/dispatch [:web|home|login|form|change-password
                              (-> % .-target .-value)])}])

;; TODO: test `.-target .-value` with :advanced compiler optimizations
;; See https://github.com/binaryage/cljs-oops

(defn on-submit
  [e]
  (.preventDefault e)
  (he/dispatch [:web|home|login|login]))

(defn form-submit
  []
  [:input
   {:type "submit"
    :value "Login"
    :on-click on-submit}])

(defn display-errors
  []
  (let [error (he/subscribe [:web|home|login|form|error])]
    [:span (str "Error: " error)]))

(defn view []
  (let [has-error? (he/subscribe [:web|home|login|form|has-error?])]
    [:div
      [:form
        [:section
          [:p [form-input-username]]
          [:p [form-input-password]]
          [:p [form-submit]]]
       (when has-error?
         [:p [display-errors]])]]))
