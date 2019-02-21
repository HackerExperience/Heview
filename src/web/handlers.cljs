(ns web.handlers
  (:require [com.smxemail.re-frame-cookie-fx :as cookie]
            [re-frame.core :refer [inject-cofx]]
            [he.core :as he]
            [web.home.handlers]
            [web.lock.handlers]
            [web.hud.handlers]
            [web.os.handlers]
            [web.wm.handlers]
            [web.apps.handlers]
            [game.account.db :as account.db]))

;; Bootstrap

(he/reg-event-fx
 :web|bootstrap
 (fn [{gdb :db} _]
   {:dispatch-n (list [:web|wm|bootstrap]
                      [:web|os|bootstrap]
                      [:web|hud|bootstrap]
                      [:web|bootstrap-ok])}))

(he/reg-event-dummy :web|bootstrap-ok)

;; Web

(he/reg-event-fx
 :web|prepare-login
 (fn [{:keys [db]} _]
   {:cookie/set {:name :account-meta
                 :value (account.db/get-username db)}
    :dispatch [:web|prepare-login-ok]}))

(he/reg-event-dummy :web|prepare-login-ok)

(he/reg-event-fx
 :web|post-init
 [(inject-cofx :cookie/get [:account-meta])]
 (fn [{db :db cookie :cookie/get} _]
   {:db (web.db/set-meta db (:account-meta cookie))}))

;; TODO: Move to a central, he(core)-based handler
(he/reg-event-dummy :cookie-set-no-on-success)
(he/reg-event-dummy :cookie-set-no-on-failure)
