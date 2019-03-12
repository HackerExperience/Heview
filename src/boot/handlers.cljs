(ns boot.handlers
  (:require [day8.re-frame.async-flow-fx]
            [he.core :as he]
            [game.db]
            [core.db]
            [boot.db]
            [boot.requests]))

(defn boot-flow
  []
  {:first-dispatch [:boot|sync],
   :rules [{:when :seen?
            :events [:boot|sync-flow-ok]
            :dispatch [:driver|sse|subscribe]}

           {:when :seen?
            :events [:driver|sse|on-open]
            :dispatch [:web|prepare-login]}

           {:when :seen?
            :events [:web|prepare-login-ok]
            :dispatch [:boot|boot-flow-ok]
            :halt? true}

           {:when :seen-any-of?,
            :events [:boot|sync-flow-fail :boot|subscribe-fail],
            :dispatch [:boot|boot-flow-fail],
            :halt? true}]})

(defn sync-flow
  [bootstrap]
  (cljs.pprint/pprint bootstrap)
  {:first-dispatch [:game|bootstrap|account (:account bootstrap)],
   :rules [{:when :seen?,
            :events [:game|bootstrap|account-ok],
            :dispatch [:game|bootstrap|servers bootstrap]}
           {:when :seen?
            :events [:game|bootstrap|servers-ok]
            :dispatch [:web|bootstrap]}
           {:when :seen?
            :events [:web|bootstrap-ok]
            :dispatch [:boot|sync-flow-ok]
            :halt? true}
           {:when :seen-any-of?,
            :events [:game|bootstrap|account-fail],
            :dispatch [:boot|sync-flow-fail],
            :halt? true}]})

(he/reg-event-fx
 :boot|subscribe
 (fn [_ _]
   (boot.requests/subscribe)))

(he/reg-event-db
 :boot|boot-flow-ok
 (fn [db _]
   (core.db/switch-mode db :boot :play boot.db/initial)))

(he/reg-event-fx
 :boot|boot-flow-fail
 (fn [{:keys [db]} _]
   {:db (boot.db/boot-flow-failed db)}))

(he/reg-event-fx
  :boot|boot-flow
  (fn [{gdb :db} [_ csrf-token]]
    (let [game-db (game.db/get-context gdb)
          new-game-db (game.db/set-csrf-token game-db csrf-token)
          new-gdb (game.db/set-context gdb new-game-db)]
      {:db (core.db/switch-mode new-gdb :home :boot {})
       :async-flow (boot-flow)})))

(he/reg-event-fx
 :boot|sync
 (fn [db _]
   (boot.requests/sync)))

(he/reg-event-fx :boot|req-sync-ok
                 (fn [db [_ fun result xargs]]
                   (fun db result xargs)))

(he/reg-event-fx :boot|req-sync-fail
                 (fn [db [_ fun result xargs]]
                   (fun db result xargs)))

(he/reg-event-fx
 :boot|sync-flow
 (fn [_ [_ bootstrap]]
    {:async-flow (sync-flow bootstrap)}))

(he/reg-event-fx
 :boot|sync-flow-ok
 (fn [_ _]
   {:dispatch [:game|post-boot-hook]}))
(he/reg-event-dummy :boot|sync-flow-fail)
(he/reg-event-dummy :boot|subscribe-ok)
(he/reg-event-dummy :boot|subscribe-fail)
