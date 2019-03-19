(ns driver.rest.handlers
  (:require [ajax.json :as ajax]
            [day8.re-frame.http-fx]
            [he.core :as he]
            [game.db]
            [driver.rest.utils :as rest.utils]))

(defn get-uri
  [path]
  (str "https://localhost:4000/v1/" path))

;; TODO: move to utils
(defn in?
  [coll element]
  (some #(= element %) coll))

(defn requires-csrf-token?
  [method path]
  (println "path is " path)
  (if (= method :get)
    false
    (cond
      (= path "login") false
      (= path "account/register") false
      :else true)))

(defn get-params
  [method path body csrf-token]
  (if (requires-csrf-token? method path)
    (merge {:_csrf-token csrf-token} body)
    body))


(he/reg-event-fx
  :driver|rest|request
  (fn [{db :db} [_ request-config]]
    (let [{method :method
           path :path
           body :body
           timeout :timeout
           resp-type :response-type
           [ok-ev & ok-params] :on-ok
           [fail-ev & fail-params] :on-fail
           csrf-token :csrf-token
           :or {body {}
                csrf-token (-> db
                               (game.db/get-context)
                               (game.db/get-csrf-token))
                timeout 5000}} request-config
          format (rest.utils/json-request-format)
          response-format (rest.utils/json-response-format :full ")]}'\n")]
      {:http-xhrio
       {:method method,
        :uri (get-uri path),
        :params (get-params method path body csrf-token),
        :with-credentials true,
        :timeout timeout
        :format format
        :response-format response-format
        :on-success [:driver|rest|on-ok-wrap ok-ev ok-params]
        :on-failure [:driver|rest|on-fail-wrap fail-ev fail-params]}})))

(defn- request-result-wrapper
  [db [_ dispatch-to [callback & rest] result]]
  (let [{status :status} result
        ;; Sometimes body is inside response.
        ;; Other times, it's outside.
        body (if (contains? result :body)
               (:body result)
               (get-in result [:response :body]))]
    {:dispatch [dispatch-to callback [status body result] rest]}))

(he/reg-event-fx :driver|rest|on-ok-wrap
                 (fn [{gdb :db} params]
                   (request-result-wrapper gdb params)))
(he/reg-event-fx :driver|rest|on-fail-wrap
                 (fn [{gdb :db} params]
                   (request-result-wrapper gdb params)))
