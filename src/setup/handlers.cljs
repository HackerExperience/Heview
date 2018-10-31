(ns setup.handlers
  (:require [day8.re-frame.async-flow-fx]
            [he.core :as he]
            [core.db]
            [setup.db]))

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
 :setup|boot-ok
 (fn [db _]
   (core.db/switch-mode db :setup :play {})))

(he/reg-event-fx
 :setup|boot-fail
 (fn [{:keys [db]} _]
   {:db (setup.db/set-boot-failed db)
    :dispatch [:driver|ws|teardown]}))

(he/reg-event-fx
  :setup|boot-flow
  (fn [{:keys [db]} [_ token account-id]]
    {:db (-> db
             (core.db/switch-mode :home :setup setup.db/initial)),
     :async-flow (boot-flow token account-id)}))
