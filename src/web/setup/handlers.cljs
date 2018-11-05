(ns web.setup.handlers
  (:require [day8.re-frame.async-flow-fx]
            [he.core :as he]
            [web.db]
            [web.setup.db :as setup.db]))

(defn boot-flow
  [token account-id]
  {:first-dispatch [:driver|ws|connect token],
   :rules [{:when :seen?,
            :events [:driver|ws|connect-ok],
            :dispatch [:driver|ws|join-account account-id]}
           {:when :seen?,
            :events [:game|bootstrap-account-ok],
            :dispatch [:driver|ws|join-servers]}
           {:when :seen?
            :events [:game|bootstrap-server-ok-all]
            :dispatch [:setup|boot-ok]
            :halt? true}
           {:when :seen-any-of?,
            :events [:game|bootstrap-account-fail],
            :dispatch [:setup|boot-fail],
            :halt? true}]})

(he/reg-event-db
 :web|setup|boot-ok
 (fn [db _]
   (web.db/switch-mode db :setup :play {})))

(he/reg-event-fx
 :web|setup|boot-fail
 (fn [{:keys [db]} _]
   {:db (setup.db/set-boot-failed db)
    :dispatch [:driver|ws|teardown]}))

(he/reg-event-fx
  :web|setup|boot-flow
  (fn [{:keys [db]} [_ token account-id]]
    {:db (-> db
             (web.db/switch-mode :home :setup web.db/initial)),
     :async-flow (boot-flow token account-id)}))
