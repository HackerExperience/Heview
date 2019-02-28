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
  (let [fun (js/eval (->js function-name))]
    (apply fun args)))

(defn get-def
  [var-name]
  (js/eval (->js var-name)))

(defn call-me-maybe
  [function-name args]
  ;; TODO: use `exists?`
  (try
    (apply (js/eval (->js function-name)) args)
    (catch :default e
      nil)))

(defn call-me-maybe-log
  [function-name args]
  (try
    (apply (js/eval (->js function-name)) args)
    (catch :default e
      (he.error/runtime (str "Log unknown function: " function-name)))))

(defn function-exists?
  [function-name]
  (not (nil? (js/eval (->js function-name)))))
