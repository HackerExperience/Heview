(ns web.install.handlers
  (:require [he.core :as he]
            [core.db]
            [web.install.db :as install.db]
            [web.install.requests :as install.requests]))

(he/reg-event-db
 :web|install|screen
 (fn [gdb [_ next-screen]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/set-screen ldb next-screen)
     (install.db/set-context gdb ldb))))

(he/reg-event-db
 :web|install|close-error-window
 (fn [gdb _]
   (as-> (install.db/get-context gdb) ldb
     (install.db/error-window-close ldb)
     (install.db/set-context gdb ldb))))

(he/reg-event-db
 :web|install|setup|restart
 (fn [gdb [_ step account-data]]
   (let [install-db (-> install.db/initial
                        (install.db/setup-restart step account-data))]
     (println install-db)
     (core.db/switch-mode gdb :home :install install-db))))


;; Setup > Step

(he/reg-event-db
 :web|install|setup|step|back
 (fn [gdb _]
   (as-> (install.db/get-context gdb) ldb
     (install.db/setup-prev-step ldb)
     (install.db/set-context gdb ldb))))

(he/reg-event-fx
 :web|install|setup|step|next
 (fn [{gdb :db} _]
   (let [ldb (install.db/get-context gdb)
         [new-ldb dispatch-ev] (install.db/setup-next-step ldb)]
     {:db (install.db/set-context gdb new-ldb)
      :dispatch-n (list dispatch-ev)})))

(he/reg-event-db
 :web|install|setup|step|input-change
 (fn [gdb [_ input-id new-value]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/setup-input-change ldb input-id new-value)
     (install.db/set-context gdb ldb))))

(he/reg-event-db
 :web|install|setup|step|input-focus
 (fn [gdb [_ input-id]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/setup-input-focus ldb input-id)
     (install.db/set-context gdb ldb))))

(he/reg-event-fx
 :web|install|setup|step|input-blur
 (fn [{gdb :db} [_ input-id]]
   (let [ldb (install.db/get-context gdb)
         [new-ldb dispatch-ev] (install.db/setup-input-blur ldb input-id)]
     {:db (install.db/set-context gdb new-ldb)
      :dispatch-n (list dispatch-ev)})))

(he/reg-event-db
 :web|install|setup|step|input-password-toggle
 (fn [gdb [_ input-id]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/setup-input-password-toggle ldb input-id)
     (install.db/set-context gdb ldb))))

;; Setup > Step > Verify

(he/reg-event-fx
 :web|install|setup|step|verify|input-change
 (fn [{gdb :db} [_ value]]
   (let [ldb (install.db/get-context gdb)
         [new-ldb dispatch-ev] (install.db/step-verify-input-change ldb value)]
     {:db (install.db/set-context gdb new-ldb)
      :dispatch-n (list dispatch-ev)})))

;; Setup > Step > Document (SignTos & SignPP) ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-db
 :web|install|setup|document|select
 (fn [gdb [_ document-id radio-id]]
   (let [ldb (install.db/get-context gdb)
         ldb (cond
               (= document-id :tos)
               (install.db/step-sign-tos-select ldb radio-id)
               (= document-id :pp)
               (install.db/step-sign-pp-select ldb radio-id))]
     (install.db/set-context gdb ldb))))

;; Setup > Step > Pricing ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-db
 :web|install|setup|step|pricing|select-plan
 (fn [gdb [_ plan-id]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/step-pricing-select-plan ldb plan-id)
     (install.db/set-context gdb ldb))))

(he/reg-event-db
 :web|install|setup|step|pricing|already-bought
 (fn [gdb _]
   (as-> (install.db/get-context gdb) ldb
     (install.db/step-pricing-already-bought ldb)
     (install.db/set-context gdb ldb))))

;; Requests > Setup > Register ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-fx
 :web|install|setup|step|register|check-username
 (fn [{gdb :db} [_ username]]
   (install.requests/setup-register-check-username username nil)))

(he/reg-event-db
 :web|install|setup|step|register|check-username-ok
 (fn [gdb [_ _ [status _]]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/step-register-check-username-ok ldb status)
     (install.db/set-context gdb ldb))))

(he/reg-event-db
 :web|install|setup|step|register|check-username-fail
 (fn [gdb [_ _ [status response]]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/step-register-check-username-fail ldb status response)
     (install.db/set-context gdb ldb))))

(he/reg-event-fx
 :web|install|setup|step|register|check-email
 (fn [{gdb :db} [_ email]]
   (install.requests/setup-register-check-email email nil)))

(he/reg-event-db
 :web|install|setup|step|register|check-email-ok
 (fn [gdb [_ _ [status _]]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/step-register-check-email-ok ldb status)
     (install.db/set-context gdb ldb))))

(he/reg-event-db
 :web|install|setup|step|register|check-email-fail
 (fn [gdb [_ _ [status response]]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/step-register-check-email-fail ldb status response)
     (install.db/set-context gdb ldb))))

(he/reg-event-fx
 :web|install|setup|step|register|submit
 (fn [{gdb :db} [_ body]]
   (install.requests/setup-register-submit body nil)))

(he/reg-event-db
 :web|install|setup|step|register|req-submit-ok
 (fn [gdb [_ _ [_ response]]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/step-register-submit-ok ldb response)
     (install.db/set-context gdb ldb))))

(he/reg-event-db
 :web|install|setup|step|register|req-submit-fail
 (fn [gdb [_ _ [status response]]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/step-register-submit-fail ldb status response)
     (install.db/set-context gdb ldb))))


;; Requests > Setup > Verify ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-fx
 :web|install|setup|step|verify|verify
 (fn [{gdb :db} [_ verification-key]]
   (install.requests/setup-verify-verify verification-key nil)))

(he/reg-event-db
 :web|install|setup|step|verify|verify-ok
 (fn [gdb [_ _ [status _]]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/step-verify-verification-ok ldb status)
     (install.db/set-context gdb ldb))))

(he/reg-event-db
 :web|install|setup|step|verify|verify-fail
 (fn [gdb [_ _ [status response]]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/step-verify-verification-fail ldb status response)
     (install.db/set-context gdb ldb))))

(he/reg-event-fx
 :web|install|setup|step|verify|verify-login
 (fn [{gdb :db} [_ verification-key]]
   (install.requests/setup-verify-verify-login verification-key nil)))

(he/reg-event-db
 :web|install|setup|step|verify|verify-login-ok
 (fn [gdb [_ _ [status response]]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/step-verify-verification-login-ok ldb status response)
     (install.db/set-context gdb ldb))))

(he/reg-event-db
 :web|install|setup|step|verify|verify-login-fail
 (fn [gdb [_ _ [status response]]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/step-verify-verification-login-fail ldb status response)
     (install.db/set-context gdb ldb))))

(he/reg-event-fx
 :web|install|setup|step|verify|check
 (fn [{gdb :db} _]
   (install.requests/setup-verify-check nil)))

(he/reg-event-db
 :web|install|setup|step|verify|check-ok
 (fn [gdb [_ _ [status response]]]
   (as-> (install.db/get-context gdb) ldb
     (install.db/step-verify-tracker-check-ok ldb status)
     (install.db/set-context gdb ldb))))

(he/reg-event-db
 :web|install|setup|step|verify|check-fail
 (fn [gdb [_ _ _]]
   gdb))

;; Requests > Setup > Documents (SignTos & SignPP) ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-fx
 :web|install|setup|document|fetch
 (fn [_ [_ document-id]]
   (install.requests/setup-document-fetch document-id :html)))

(he/reg-event-db
 :web|install|setup|document|fetch-ok
 (fn [gdb [_ document-id [_status resp]]]
   (let [ldb (install.db/get-context gdb)
         ldb (cond
               (= document-id :tos) (install.db/step-sign-tos-fetch ldb resp)
               (= document-id :pp) (install.db/step-sign-pp-fetch ldb resp))]
     (install.db/set-context gdb ldb))))

(he/reg-event-db
 :web|install|setup|document|fetch-fail
 (fn [gdb [_ _ [status _] f]]
   (println "Fetch fail")
   gdb))

(he/reg-event-fx
 :web|install|setup|document|sign
 (fn [_ [_ document-id revision-id csrf-token]]
   (install.requests/setup-document-sign document-id revision-id csrf-token)))

(he/reg-event-db
 :web|install|setup|document|sign-ok
 (fn [gdb [_ document-id [status resp]]]
   (let [ldb (install.db/get-context gdb)
         ldb (cond
               (= document-id :tos)
               (install.db/step-sign-tos-sign-document-ok ldb resp status)
               (= document-id :pp)
               (install.db/step-sign-pp-sign-document-ok ldb resp status))]
     (install.db/set-context gdb ldb))))

(he/reg-event-db
 :web|install|setup|document|sign-fail
 (fn [gdb [_ document-id [status resp]]]
   (let [ldb (install.db/get-context gdb)
         ldb (cond
               (= document-id :tos)
               (install.db/step-sign-tos-sign-document-fail ldb status resp)
               (= document-id :pp)
               (install.db/step-sign-pp-sign-document-fail ldb status resp))]
     (install.db/set-context gdb ldb))))
