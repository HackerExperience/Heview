(ns web.apps.browser.page.utils)

(defn parse
  [base-url opts]
  (let [path (get opts :path "/")
        input-url (str base-url path)]
    {:base-url base-url
     :input-url input-url
     :path path}))
