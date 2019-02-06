(ns web.lock.view
  (:require [he.core :as he]))

(defn password-form-input
  []
  [:input
   {:type "password"
    :placeholder "Password"
    :value (he/subscribe [:web|lock|form|password])
    :on-change #(he/dispatch [:web|lock|form|set-password
                              (-> % .-target .-value)])}])

(defn on-password-form-submit
  [e]
  (.preventDefault e)
  (he/dispatch [:web|lock|form|login]))

(defn password-form-submit
  []
  [:input {:type "submit" :value "->" :on-click on-password-form-submit}])

(defn password-form-loading
  []
  [:div "Loading..."])

(defn password-form
  []
  (let [fail-reason (he/subscribe [:web|lock|fail-reason])
        loading? (he/subscribe [:web|lock|loading?])]
    [:div (str fail-reason)
     (println loading?)
     (if-not loading?
       [:form
        [:p [password-form-input]]
        [:p [password-form-submit]]]
       [password-form-loading])]))

(defn passwordless-form
  []
  (let [loading? (he/subscribe [:web|lock|loading?])]
    (if loading?
      [:div "Loading..."]
      [:button {:on-click #(he/dispatch [:web|lock|check-session])} "->"])))

(defn login-control
  []
  (let [failed? (he/subscribe [:web|lock|failed?])]
    [:div
    (if failed?
      (password-form)
      (passwordless-form))]))

(defn view
  []
  (let [username (he/subscribe [:web|meta|username])]
    [:div (str "Welcome back, " username)
     [login-control]]))
