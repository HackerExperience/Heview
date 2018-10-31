(ns driver.rest.handlers
  (:require [ajax.json :as ajax]
            [day8.re-frame.http-fx]
            [he.core :as he]))

;; 

(he/reg-event-fx
  :driver|rest|request
  (fn [_
       [_ method path body
        {[ok-ev & ok-params] :on-ok, [fail-ev & fail-params] :on-fail}]]
    {:http-xhrio {:method method,
                  :uri (str "https://localhost:4000/v1/" path),
                  :params body,
                  :timeout 5000,
                  :format (ajax/json-request-format),
                  :response-format (ajax/json-response-format {:keywords? true}),
                  :on-success [ok-ev ok-params],
                  :on-failure [fail-ev fail-params]}}))
