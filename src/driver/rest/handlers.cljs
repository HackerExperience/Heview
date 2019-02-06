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
  [path]
  (cond
    (= path "ping") false
    (= path "login") false
    (= path "check-session") false
    :else true))

(defn get-params
  [path body csrf-token]
  (if (requires-csrf-token? path)
    (merge {:_csrf-token csrf-token} body)
    body))

(he/reg-event-fx
  :driver|rest|request
  (fn [{:keys [db]}
       [_ method path response-type body
        {[ok-ev & ok-params] :on-ok, [fail-ev & fail-params] :on-fail}]]
    (let [csrf-token (game.db/get-csrf-token db)] ;; todo: put this on rest.db
      {:http-xhrio
       {:method method,
        :uri (get-uri path),
        :params (get-params path body csrf-token),
        :with-credentials true,
        :timeout 5000,
        :format (rest.utils/json-request-format),
        :response-format (rest.utils/json-response-format response-type ")]}'\n"),
        :on-success [ok-ev ok-params],
        :on-failure [:driver|rest|on-fail-wrap fail-ev fail-params]}})))

(he/reg-event-fx :driver|rest|on-fail-wrap
                 (fn [{:keys [db]} [_ dispatch-to dispatch-params response]]
                   {:dispatch [dispatch-to dispatch-params response]}))
