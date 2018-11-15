(ns boot.handlers
  (:require [day8.re-frame.async-flow-fx]
            [he.core :as he]
            [core.db]
            [boot.db]))

(defn boot-flow
  [session-id account-id bootstrap]
  {:first-dispatch [:game|bootstrap|account (:account bootstrap)],
   :rules [{:when :seen?,
            :events [:game|bootstrap|account-ok],
            :dispatch [:game|bootstrap|servers (:servers bootstrap)]}
           {:when :seen?
            :events [:game|bootstrap|servers-ok]
            :dispatch [:web|bootstrap]}
           {:when :seen?
            :events [:web|bootstrap-ok]
            :dispatch [:boot|flow-ok]
            :halt? true}
           {:when :seen-any-of?,
            :events [:game|bootstrap|account-fail],
            :dispatch [:boot|flow-fail],
            :halt? true}]})

(he/reg-event-db
 :boot|flow-ok
 (fn [db _]
   (core.db/switch-mode db :boot :play boot.db/initial)))

(he/reg-event-fx
 :boot|flow-fail
 (fn [{:keys [db]} _]
   {:db (boot.db/boot-flow-failed db)}))

(he/reg-event-fx
  :boot|flow
  (fn [{:keys [db]} [_ session-id account-id bootstrap]]
    {:db (-> db
             (core.db/switch-mode :home :boot {}))
     :async-flow (boot-flow session-id account-id bootstrap)}))

