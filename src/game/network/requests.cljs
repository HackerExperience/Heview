(ns game.network.requests
  (:require [driver.rest.request :as request]))

(defn on-browse-ok
  [_db [status response] [{callback :on-ok}]]
  (request/wrap-callback callback status response))

(defn on-browse-fail
  [_db [status response] [{callback :on-fail}]]
  (request/wrap-callback callback status response))

(defn browse
  [server-cid ip callback]
  (request/browse
   server-cid ip
   {:on-ok [:game|network|req-browse-ok on-browse-ok callback]
    :on-fail [:game|network|req-browse-fail on-browse-fail callback]}))
