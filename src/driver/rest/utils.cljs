(ns driver.rest.utils
  (:require [ajax.json]
            [ajax.protocols :refer [-body -status -get-all-headers]]))


(defn strip-prefix [^String prefix text]
  (if (and prefix (= 0 (.indexOf text prefix)))
    (.substring text (.-length prefix))
    text))

(defn json-request-format
  []
  (ajax.json/json-request-format))

(defn read-json-body
  [prefix body]
  (let [json-js (.parse js/JSON (strip-prefix prefix body))]
    (js->clj json-js :keywordize-keys true)))

(defmulti json-response-format
  (fn [format-type params]
    format-type))

(defmethod json-response-format :full
  [_ prefix]
  {:read (fn read-response [xhrio]
           {:status (-status xhrio)
            :headers (-get-all-headers xhrio)
            :body (read-json-body prefix (-body xhrio))})
   :content-type ["application/json"]
   :description "Custom JSON (full)"})

(defmethod json-response-format :simple
  [_ prefix]
  {:read (fn read-response [xhrio]
           (read-json-body prefix (-body xhrio)))
   :content-type ["application/json"]
   :description "Custom JSON (simple)"})

