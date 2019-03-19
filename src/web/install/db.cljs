(ns web.install.db
  (:require [clojure.string :as str]
            [cljs.core.match :refer-macros [match]]
            [he.core :as he]))

;; Context

(defn get-context
  [global-db]
  (get-in global-db [:install]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:install] updated-local-db))

;; Validator

(defn step-register-validate-username
  [username]
  (let [len (count username)
        error-msg (cond
                    (< len 3) "Username must be at least 3 characters long"
                    (> len 15) "Username must be at most 15 characters long"
                    (.test #"[^a-zA-Z0-9]" username) "Username must contain only azAZ09"
                    :else nil)
        valid? (nil? error-msg)
        input-class (if valid? :waiting :error)
        event (when valid?
                [:web|install|setup|step|register|check-username username])]
    [{:valid? valid? :error-msg error-msg :input-class input-class} event]))

(defn step-register-validate-password
  [password]
  (let [len (count password)
        error-msg (cond
                    (< len 6) "Password must be at least 6 characters long"
                    :else nil)
        valid? (nil? error-msg)
        input-class (if valid? :ok :error)]
    [{:valid? valid? :error-msg error-msg :input-class input-class} nil]))

(defn step-register-validate-email
  [email]
  (let [len (count email)
        error-msg (cond
                    (< len 3) "Email seems to be invalid"
                    (not (str/includes? email "@")) "Email seems to be invalid"
                    :else nil)
        valid? (nil? error-msg)
        input-class (if valid? :waiting :error)
        event (when valid?
                [:web|install|setup|step|register|check-email email])]
    [{:valid? valid? :error-msg error-msg :input-class input-class} event]))

;; Model

(def initial-setup-steps
  {:register {:next-step :verify
              :prev-step nil
              :current-input :username
              :header-title "X.OS Setup - Account creation"
              :desc-title "Create your account"
              :desc-text "Register a new account in order to play Hacker Experience. Make sure to type a valid email address."
              :input-text "Enter your user information in order to create an account and play Hacker Experince."
              :inputs [:username :email :password]
              :username {:id :username
                         :type :text
                         :value ""
                         :label "Username:"
                         :error-msg nil
                         :input-class nil
                         :input-text "Type your in-game username."
                         :validate-fn :register-username}
              :email {:id :email
                      :type :text
                      :value ""
                      :label "Email Address:"
                      :error-msg nil
                      :input-class nil
                      :input-text "Type your email. We'll ask you to verify your email address."
                      :validate-fn :register-email}
              :password {:id :password
                         :type :password
                         :value ""
                         :label "Password:"
                         :error-msg nil
                         :input-class nil
                         :input-text "Type your in-game password. Must be at least 6 characters long."
                         :validate-fn :register-password}
              :loading? false
              :request-error-msg nil}
   :verify {:next-step :sign-tos
            :header-title "X.OS Setup - Email verification"
            :desc-title "Verify your email"
            :desc-text "We just sent you a verification code. Please paste the code here or click the link we sent you."
            :input-text "In order to prevent bots, we have to verify your email address. We just sent you an email with a verification code and a link. You can either type the code here, or click the link we just sent."
            :prev-step nil}
   :sign-tos {:next-step :sign-pp
              :header-title "X.OS Setup - Terms of Service"
              :desc-title "License Agreement"
              :desc-text "Review the terms for using Hacker Experience."
              :input-text "Please read the following Terms of Use. To start playing Hacker Experience, you must accept the Terms of Use."
              :input-selected nil
              :content nil
              :revision-id nil
              :loading? false}
   :sign-pp {:next-step :pricing
             :header-title "X.OS Setup - Privacy Policy"
             :desc-title "License Agreement"
             :desc-text "Review the terms for using Hacker Experience."
             :input-text "Please read the following Privacy Policy. To start playing Hacker Experience, you must accept the Privacy Policy."
             :input-selected nil
             :content nil
             :revision-id nil
             :loading? false}
   :pricing {:next-step :finish
             :header-title "X.OS Setup - Pricing"
             :desc-title "Pricing"
             :desc-text "Please select your in-game plan."
             :input-text ""
             :selected-plan nil}
   :billing {}
   :finish {:next-step nil
            :prev-step nil
            :header-title "X.OS Setup - Finish"
            :desc-title "Welcome"
            :desc-text "This is the end of your account setup. Welcome to Hacker Experience!"
            :input-text ""}
   })

(def initial
  {:screen :bsod
   :error-window nil
   :setup {:steps initial-setup-steps
           :active-step :register
           :account nil}
   :welcome {:page :main
             :state {}}})

(defn initial-verify
  [verification-key]
  (js/setTimeout
   #(he/dispatch [:web|install|setup|step|verify|verify-login verification-key])
   20)
  (-> initial
      (assoc :screen :setup)
      (assoc-in [:setup :active-step] :verify)
      (assoc-in [:setup :steps :verify :verification-key] verification-key)
      (assoc-in [:setup :steps :verify :verifying?] true)))

(defn get-screen
  [db]
  (:screen db))

(defn set-screen
  [db screen]
  (assoc db :screen screen))

(defn error-window-close
  [db]
  (assoc db :error-window nil))

;; Setup > Register

(defn step-register-std-fail-msg
  [type value options?]
  [:div
   [:span (str "The " (name type) " ")]
   [:span.ui-g-bold value]
   [:span " is already taken."]
   (when options?
     [:div
      [:span.inst-m-b-w-c-link
       {:style {:padding-top "5px"}
        :on-click #(he/dispatch [:web|install|redirect-login])}
       "Login instead?"]
      [:span " | "]
      [:span.inst-m-b-w-c-link
       {:on-click #(he/dispatch [:web|install|redirect-forgot-password])}
       "Forgot your password?"]])])

(defn step-register-check-username-ok
  [db status]
  (if (= status 200)
    (-> db
        (assoc-in [:setup :steps :register :username :error-msg] nil)
        (assoc-in [:setup :steps :register :username :input-class] :ok))
    db))

(defn step-register-check-username-fail
  [db status response]
  (let [username (get-in db [:setup :steps :register :username :value])
        error-msg (if (= status 403)
                    (step-register-std-fail-msg :username username false)
                    (str "Internal server error: " (:error response)))]
    (-> db
        (assoc-in [:setup :steps :register :username :error-msg] error-msg)
        (assoc-in [:setup :steps :register :username :input-class] :error))))

(defn step-register-check-email-ok
  [db status]
  (if (= status 200)
    (-> db
        (assoc-in [:setup :steps :register :email :error-msg] nil)
        (assoc-in [:setup :steps :register :email :input-class] :ok))
    db))

(defn step-register-check-email-fail
  [db status response]
  (let [email (get-in db [:setup :steps :register :email :value])
        error-msg (if (= status 403)
                    (step-register-std-fail-msg :email email true)
                    (str "Internal server error: " (:error response)))]
    (-> db
        (assoc-in [:setup :steps :register :email :error-msg] error-msg)
        (assoc-in [:setup :steps :register :email :input-class] :error))))

(defn step-register-submit-ok
  [db response]
  (let [step (get-in db [:setup :steps :register])
        setup-account {:username (get-in step [:username :value])
                       :email (get-in step [:email :value])
                       :account-id (:account_id response)
                       :csrf-token (:csrf_token response)}]
    (-> db
        (assoc-in [:setup :steps :register :loading?] false)
        (assoc-in [:setup :steps :register :request-error-msg] nil)
        (assoc-in [:setup :account] setup-account)
        (assoc-in [:setup :active-step] :verify))))

(defn step-register-submit-fail
  [db status {{reason :reason} :error}]
  (let [error-msg
        (cond
          (>= status 500) "Account creation failed - Internal server error"
          (= 400 status) "Account creation failed - Bad request"
          :else (str "Account creation failed with message: " reason))]
    (-> db
        (assoc-in [:setup :steps :register :loading?] false)
        (assoc-in [:setup :steps :register :request-error-msg] error-msg))))

(defn step-register-next-step-submit
  [db step]
  (let [new-db (assoc-in db [:setup :steps :register :loading?] true)
        body {:username (get-in step [:username :value])
              :password (get-in step [:password :value])
              :email (get-in step [:email :value])}]
    [new-db [:web|install|setup|step|register|submit body]]))

(defn step-register-next-step
  [db]
  (let [step (get-in db [:setup :steps :register])]
    (cond
      (nil? (:request-status step)) (step-register-next-step-submit db step)


      )))

;; Setup > Verify

(defn do-step-verify-input-change
  [db new-value]
  (let [new-db (assoc-in db [:setup :steps :verify :verification-key] new-value)
        event (if (= (count new-value) 6)
                [:web|install|setup|step|verify|verify new-value])]
    (if (= (count new-value) 6)
      [(-> new-db
           (assoc-in [:setup :steps :verify :verifying?] true)
           (assoc-in [:setup :steps :verify :error-msg] nil))
       [:web|install|setup|step|verify|verify new-value]]
      [new-db nil])))

(defn step-verify-input-change
  [db new-value]
  (let [verify-result (get-in db [:setup :steps :verify :verify-result])]
    (cond
      (> (count new-value) 6) [db nil]
      (= verify-result :ok) [db nil]
      :else (do-step-verify-input-change db new-value))))

(defn step-verify-verification-ok
  [db _status]
  (js/setTimeout #(he/dispatch [:web|install|setup|step|next :not-nil]) 1500)
  (-> db
      (assoc-in [:setup :steps :verify :verifying?] false)
      (assoc-in [:setup :steps :verify :verify-result] :ok)))

(defn step-verify-verification-fail
  [db status response]
  (let [current-key (get-in db [:setup :steps :verify :verification-key])
        error-msg (cond
                    (= status 404) (str "The key " current-key " is wrong or invalid.")
                    :else (str "Internal error: " (:error response)))]
    (-> db
        (assoc-in [:setup :steps :verify :verifying?] false)
        (assoc-in [:setup :steps :verify :error-msg] error-msg)
        (assoc-in [:setup :steps :verify :verify-result] :error))))

(defn step-verify-verification-login-ok
  [db status response]
  (let [account {:account-id (:account_id response)
                 :csrf-token (:csrf_token response)
                 :username (:username response)}]
    (-> db
        (assoc-in [:setup :account] account)
        (step-verify-verification-ok status))))

(defn step-verify-verification-login-fail
  [db status response]
  (let [close-ev [:web|install|redirect-home]
        error-window {:message "Wrong verification key. Please proceed to the home page and login with your username and password."
                      :title "Wrong key"
                      :on-ok close-ev
                      :on-close close-ev}]
    (-> db
        (assoc-in [:setup :steps :verify :verifying?] false)
        (assoc-in [:setup :steps :verify :verify-result] :error)
        (assoc :error-window error-window))))

(defn step-verify-tracker-check-ok
  "The tracker automatically checks whether the email has been verified. This is
  useful because the user may verify the email by clicking on the link sent to
  them, which will open the installation on a new page and the user will
  probably keep registering on that other page.
  But, if the user verifies the email by typing the code, there is a small
  chance (depends on a specific timing condition) that the tracker will return
  ok, essentially confirming that the verification was done (as expected), but
  the verification was done by the user on that same page. Confusing, I know.
  In short, we make sure that the user is still on the `verify` step. If so, we
  proceed to the next one. If not, it means the user has already proceeded to
  the `sign-tos` step and we should ignore this event. Peace."
  [db status]
  (if (= (get-in db [:steps :active-step]) :verify)
    (step-verify-verification-ok db status)
    db))

(defn step-verify-next-step
  [db]
  [(-> db (assoc-in [:setup :active-step] :sign-tos)) nil])

;; Setup > Sign-tos

(defn step-sign-block-next-step
  [db name]
  (let [close-ev [:web|install|close-error-window]
        error-window {:message (str "You must accept the " name " in order to play Hacker Experience.")
                      :title "he.exe - Legal error"
                      :on-ok close-ev
                      :on-close close-ev}]
    [(-> db
         (assoc :error-window error-window))
     nil]))

(defn step-sign-tos-block-next-step
  [db]
  (step-sign-block-next-step db "Terms of Service"))

(defn step-sign-tos-sign-document
  [db]
  (let [revision-id (get-in db [:setup :steps :sign-tos :revision-id])
        csrf-token (get-in db [:setup :account :csrf-token])]
    [(assoc-in db [:setup :steps :sign-tos :loading?] true)
     [:web|install|setup|document|sign :tos revision-id csrf-token]]))

(defn step-sign-tos-sign-document-ok
  [db _status _response]
  (-> db
      (assoc-in [:setup :steps :sign-tos :loading?] false)
      (assoc-in [:setup :active-step] :sign-pp)))

(defn step-sign-tos-sign-document-fail
  [db status response]
  (let [close-ev [:web|install|close-error-window]
        error-window {:message (str "Oops. Something went wrong with the document signature. Server returned " (:error response) " - Code: " status)
                      :title "he.exe - Catastrophic failure"
                      :on-ok close-ev
                      :on-error close-ev}]
    (-> db
        (assoc-in [:setup :steps :sign-tos :loading?] false)
        (assoc :error-window error-window))))

(defn step-sign-tos-next-step
  [db]
  (let [content (get-in db [:setup :steps :sign-tos :content])
        input-selected (get-in db [:setup :steps :sign-tos :input-selected])]
    (cond
      (nil? input-selected) (step-sign-tos-block-next-step db)
      (nil? content) (step-sign-tos-block-next-step db)
      (= :not-accept input-selected) (step-sign-tos-block-next-step db)
      (= :accept input-selected) (step-sign-tos-sign-document db))))

(defn step-sign-tos-select
  [db radio-id]
  (assoc-in db [:setup :steps :sign-tos :input-selected] radio-id))

(defn step-sign-tos-fetch
  [db response]
  (-> db
      (assoc-in [:setup :steps :sign-tos :revision-id] (:revision_id response))
      (assoc-in [:setup :steps :sign-tos :content] (:content response))
      (assoc-in [:setup :steps :sign-tos :input-selected] nil)))

;; Setup > Sign-pp

(defn step-sign-pp-block-next-step
  [db]
  (step-sign-block-next-step db "Privacy Policy"))

(defn step-sign-pp-sign-document
  [db]
  (let [revision-id (get-in db [:setup :steps :sign-pp :revision-id])
        csrf-token (get-in db [:setup :account :csrf-token])]
    [(assoc-in db [:setup :steps :sign-pp :loading?] true)
     [:web|install|setup|document|sign :pp revision-id csrf-token]]))

(defn step-sign-pp-sign-document-ok
  [db _status _response]
  (-> db
      (assoc-in [:setup :steps :sign-pp :loading?] false)
      (assoc-in [:setup :active-step] :pricing)))

(defn step-sign-pp-sign-document-fail
  [db status response]
  (let [close-ev [:web|install|close-error-window]
        error-window {:message (str "Oops. Something went wrong with the document signature. Server returned " (:error response) " - Code: " status)
                      :title "he.exe - Catastrophic failure"
                      :on-ok close-ev
                      :on-error close-ev}]
    (-> db
        (assoc-in [:setup :steps :sign-pp :loading?] false)
        (assoc :error-window error-window))))

(defn step-sign-pp-next-step
  [db]
  (let [content (get-in db [:setup :steps :sign-pp :content])
        input-selected (get-in db [:setup :steps :sign-pp :input-selected])]
    (cond
      (nil? input-selected) (step-sign-pp-block-next-step db)
      (nil? content) (step-sign-pp-block-next-step db)
      (= :not-accept input-selected) (step-sign-pp-block-next-step db)
      (= :accept input-selected) (step-sign-pp-sign-document db))))

(defn step-sign-pp-select
  [db radio-id]
  (assoc-in db [:setup :steps :sign-pp :input-selected] radio-id))

(defn step-sign-pp-fetch
  [db response]
  (-> db
      (assoc-in [:setup :steps :sign-pp :revision-id] (:revision_id response))
      (assoc-in [:setup :steps :sign-pp :content] (:content response))
      (assoc-in [:setup :steps :sign-pp :input-selected] nil)))

;; Setup > Pricing

(defn step-pricing-block-next-step
  [db]
  (let [close-ev [:web|install|close-error-window]
        error-window {:message "Please select a plan. You can play Hacker Experience for free, just select the 'Free' plan."
                      :title "he.exe - Please select a plan"
                      :on-ok close-ev
                      :on-error close-ev}]
    [(assoc db :error-window error-window) nil]))

(defn step-pricing-proceed-next-step
  [db]
  [(assoc-in db [:setup :active-step] :finish) nil])

(defn step-pricing-next-step
  [db]
  (let [selected-plan (get-in db [:setup :steps :pricing :selected-plan])]
    (cond
      (nil? selected-plan) (step-pricing-block-next-step db)
      (= :free selected-plan) (step-pricing-proceed-next-step db)
      :else (step-pricing-proceed-next-step db))))

(defn step-pricing-select-plan
  [db plan-id]
  (assoc-in db [:setup :steps :pricing :selected-plan] plan-id))

(defn step-pricing-already-bought
  [db]
  (let [close-ev [:web|install|close-error-window]
        error-window {:message "Those who bought the game on Indiegogo or via our webiste have the Lifetime plan. Once we start the Beta version, you'll be asked to confirm your purchase either via your order ID or your order email."
                      :title "Thank you for your support!"
                      :on-ok close-ev
                      :on-error close-ev}]
    (assoc db :error-window error-window)))

;; Setup > Step > Finish ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn step-finish-next-step
  [db]
  [db [:boot|boot-flow :install (get-in db [:setup :account :csrf-token])]])

;; Setup

(defn setup-restart
  [db step account]
  (-> db
      (assoc-in [:setup :account] account)
      (assoc-in [:setup :active-step] step)
      (assoc :screen :setup)))

(defn setup-validate-input
  [validate-fn value]
  (match validate-fn
         :register-username (step-register-validate-username value)
         :register-password (step-register-validate-password value)
         :register-email (step-register-validate-email value)))

(defn setup-get-active-step
  [db]
  (get-in db [:setup :active-step]))

(defn setup-handle-next-step
  [db]
  (match (setup-get-active-step db)
         :register (step-register-next-step db)
         :verify (step-verify-next-step db)
         :sign-tos (step-sign-tos-next-step db)
         :sign-pp (step-sign-pp-next-step db)
         :pricing (step-pricing-next-step db)
         :finish (step-finish-next-step db)
         ))

(defn setup-prev-step
  [db]
  ;; (assoc-in db [:setup :active-step]))
  db)

(defn setup-next-step
  [db]
  (setup-handle-next-step db))

(defn setup-input-change
  [db input-id new-value]
  (let [active-step (setup-get-active-step db)
        input (get-in db [:setup :steps active-step input-id])
        validate-fn (:validate-fn input)
        new-input (if-not (nil? (:error-msg input))
                    (merge input (setup-validate-input validate-fn new-value))
                    input)]
    (assoc-in db
              [:setup :steps active-step input-id]
              (assoc new-input :value new-value))))

(defn setup-input-focus
  [db input-id]
  (let [active-step (setup-get-active-step db)]
    (-> db
        (assoc-in [:setup :steps active-step :current-input] input-id))))

(defn setup-input-blur
  [db input-id]
  (let [active-step (setup-get-active-step db)
        input (get-in db [:setup :steps active-step input-id])
        validate-fn (:validate-fn input)
        value (:value input)
        [validation-map event] (setup-validate-input validate-fn value)
        new-input (merge input validation-map)]
    [(-> db
         (assoc-in [:setup :steps active-step :current-input] nil)
         (assoc-in [:setup :steps active-step input-id] new-input))
     event]))

(defn setup-input-password-toggle
  [db input-id]
  (let [active-step (setup-get-active-step db)
        current-type (get-in db [:setup :steps active-step input-id :type])
        new-type (if (= current-type :password) :text :password)]
    (-> db
        (assoc-in [:setup :steps active-step input-id :type] new-type))))
