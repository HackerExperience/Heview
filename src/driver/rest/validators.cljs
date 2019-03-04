(ns driver.rest.validators
  (:require [taoensso.truss :refer-macros [have?]]
            [he.utils.validators :as utils.v]))

(defn v [fun x]
  (utils.v/v fun x))
(defn v-each [fun & args]
  (utils.v/v-each fun args))

(defn request-arg-string [x]
  (have? string? x))

(defn request-custom [x]
  (and (have? [:ks>= #{:on-ok :on-fail}] x)
       (have? vector? (:on-ok x))
       (have? vector? (:on-fail x))))

(defn request-build-params
  [[method path body custom]]
  (and (have? (utils.v/in? [:get :post] method))
       (have? string? path)
       (have? map? body)
       (request-custom custom)))

(defn request-callback [x]
  (have? keyword? x))

(defn request-callback-gargs [x]
  (have? map? x))

(defn request-status [x]
  "Handle status code within the range we work with"
  (and (have? int? x)
       (utils.v/have-true? (or (<= 200 x 520)
                               (= x -1)))))
