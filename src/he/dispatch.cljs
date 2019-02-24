(ns he.dispatch
  "Sometimes you gotta do whatever you gotta do to get things working. HACK!
  (Fortunately this is a hacking game?)"
  (:require [clojure.string :as str]))

(defn ->js [var-name]
  (-> var-name
      (str/replace #"/" ".")
      (str/replace #"-" "_")))

(defn call
  [function-name args]
  (println function-name)
  (let [fun (js/eval (->js function-name))]
    (apply fun args)))

(defn call-me-maybe
  [function-name args]
  (try
    (apply (js/eval (->js function-name)) args)
    (catch :default e
      nil)))
