(ns web.install.view
  (:require [cljs.core.match :refer-macros [match]]
            [reagent.core :as r]
            [he.core :as he]
            [web.os.view.bsod :as bsod.view]
            [web.boot.view :as boot.view]))

(defn- random-key []
  (int (* 1000 (.random js/Math))))

;; NOTE to self: Do not get angry: Most of the components here are completely
;; rerendered every time they change (even inputs `on-change`). Turns out this
;; is a very isolated part of the game, and the full re-rendering does not
;; affect performance in any distinguishable way. But it does save my time, so
;; doing TheRightWay is TODO.

(defn on-window-back
  [prev-step _event]
  (he/dispatch [:web|install|setup|step|back prev-step]))

(defn on-window-next
  [next-step _event]
  (he/dispatch [:web|install|setup|step|next next-step]))

(defn render-window
  [{header-title :header-title
    desc-title :desc-title
    desc-text :desc-text
    content-hiccup :content-hiccup
    prev-step :prev-step
    next-step :next-step
    next-step-str :next-step-str
    loading? :loading?
    width :width
    height :height
    extra-button-name :extra-button-name
    extra-button-fn :extra-button-fn
    :or {width "600px"
         height "430px"
         next-step-str "Next >"}}]
  [:div.inst-m-b-window
   {:style {:width width :height height}}
   [:div.inst-m-b-w-header header-title]
   [:div.inst-m-b-w-subheader
    [:div.inst-m-b-w-sh-description
     [:div.inst-m-b-w-sh-desc-title desc-title]
     [:div.inst-m-b-w-sh-desc-text desc-text]]
    [:div.inst-m-b-w-sh-icon
     [:i.fas.fa-compact-disc]]]
   [:div.inst-m-b-w-subheader-separator]
   [:div.inst-m-b-w-content
    (content-hiccup)]
   [:div.inst-m-b-w-footer-separator]
   [:div.inst-m-b-w-footer
    (when-not (nil? extra-button-name)
      [:div.inst-m-b-w-f-extra-button
       [:button.inst-m-b-w-f-button
        {:on-click #(extra-button-fn %)}
        extra-button-name]])
    [:div.inst-m-b-w-f-buttons
     [:button.inst-m-b-w-f-button.inst-m-b-w-f-button-disabled
      {:on-click #(on-window-back prev-step %)}
      "< Back"]
     [:button.inst-m-b-w-f-button
      {:on-click #(on-window-next next-step %)}
      next-step-str]]
    [:div.inst-m-b-w-f-loading
     (when loading?
       [:i.fa.fa-spinner.fa-spin])]]])

;; Setup > Step > Register ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn on-step-input-change
  [id event]
  (he/dispatch [:web|install|setup|step|input-change
                id (-> event .-target .-value)]))

(defn on-step-input-blur
  [id _event]
  (he/dispatch [:web|install|setup|step|input-blur id]))

(defn on-step-input-focus
  [id _event]
  (he/dispatch [:web|install|setup|step|input-focus id]))

(defn on-step-input-password-toggle
  [id _event]
  (he/dispatch [:web|install|setup|step|input-password-toggle id]))

(def input-class-ok "inst-s-input-ok")
(def input-class-error "inst-s-input-error")
(def input-badge-ok
  [:div.inst-s-input-badge.inst-s-input-badge-ok
   [:i.far.fa-check-circle]])
(def input-badge-waiting
  [:i.inst-s-input-badge.inst-s-input-badge-waiting
   [:i.fa.fa-spinner.fa-spin]])
(def input-badge-error
  [:i.inst-s-input-badge.inst-s-input-badge-error
   [:i.fas.fa-exclamation-circle]])

(defn step-register-content-input
  [input]
  (let [current-input-class (:input-class input)
        [input-class input-badge]
        (cond
          (nil? current-input-class) ["" nil]
          (= current-input-class :ok) [input-class-ok input-badge-ok]
          (= current-input-class :waiting) [input-class-ok input-badge-waiting]
          (= current-input-class :error) [input-class-error input-badge-error])]
  [:div.inst-s-r-m-f-entry
   [:div.inst-s-r-m-f-e-label
    [:span (:label input)]]
   [:div.inst-s-r-m-f-e-input.inst-s-input-area
    [:input.inst-s-input
     {:id (str "inst-s-input-" (name (:id input)))
      :class input-class
      :type (:type input)
      :on-change #(on-step-input-change (:id input) %)
      :on-focus #(on-step-input-focus (:id input) %)
      :on-blur #(on-step-input-blur (:id input) %)
      :value (:value input)}]
    (when-not (nil? input-badge)
      input-badge)
    (when (and (= (:id input) :password)
               (not (= (:value input) "")))
      [:div.inst-s-input-eye
       {:on-click #(on-step-input-password-toggle (:id input) %)}
       (if (= (:type input) :password)
         [:i.fas.fa-eye]
         [:i.fas.fa-eye-slash])])]]))

(defn step-register-content-input-error
  [input]
  (when-not (nil? (:error-msg input))
    [:div.inst-s-r-m-f-error
     [:i.fas.fa-exclamation-circle]
     [:span (:error-msg input)]]))

(defn step-register-content
  [step input-text]
  [:div.inst-step.inst-step-register
   [:div.inst-s-r-icon
    [:i.fa.fa-user-circle]]
   [:div.inst-s-r-main
    [:div.inst-s-r-m-title input-text]
    [:div.inst-s-r-m-form
     (for [input (:inputs step)]
       ^{:key (str "input-register-entry-" input)}
       [step-register-content-input (get step input)])
     [:div.inst-s-r-m-f-errors
      (for [input (:inputs step)]
        ^{:key (str "input-register-error-" input)}
        [step-register-content-input-error (get step input)])
      (when-not (nil? (:request-error-msg step))
        [:div.inst-s-r-m-f-error
         [:i.fas.fa-exclamation-circle]
         [:span (:request-error-msg step)]])]]]])

(defn step-register-focus-username []
  (js/setTimeout
   #(.focus (.getElementById js/document "inst-s-input-username")) 50))

(defn view-step-register []
  (let [step (he/subscribe [:web|install|setup|step|register])
        current-input-id (:current-input step)
        current-input (get step current-input-id)
        header-title (:header-title step)
        input-text (if-not (nil? current-input-id)
                     (:input-text current-input)
                     (:input-text step))
        loading? (:loading? step)]
    [render-window
     {:header-title header-title
      :desc-title (:desc-title step)
      :desc-text (:desc-text step)
      :content-hiccup (partial step-register-content step input-text)
      :next-enabled? false
      :next-step (:next-step step)
      :prev-step (:prev-step step)
      :loading? loading?}]))

;; Setup > Step > Verify ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn on-step-verify-input-change
  [event]
  (he/dispatch [:web|install|setup|step|verify|input-change
                (-> event .-target .-value)]))

(defn step-verify-content-input
  [value]
  [:div.inst-s-r-m-f-entry
   [:div.inst-s-r-m-f-e-label
    [:span "Verification code"]]
   [:div.inst-s-r-m-f-e-input.inst-s-input-area
    [:input.inst-s-input
     {:value value
      :on-change #(on-step-verify-input-change %)}]]])

(defn step-verify-result-ok []
  [:div.inst-s-v-m-result-success
   [:i.far.fa-check-circle]])
(defn step-verify-result-error []
  [:div.inst-s-v-m-result-error
   [:i.fas.fa-exclamation-circle]])

(defn step-verify-content
  [account step]
  (let [input-text (:input-text step)
        email (:email account)
        verify-result (:verify-result step)]
    [:div.inst-step.inst-step-register
     [:div.inst-s-r-icon
      (if (= :ok verify-result)
        [:i.far.fa-envelope-open]
        [:i.far.fa-envelope])]
     [:div.inst-s-r-main
      [:div.inst-s-r-m-title input-text]
      [:div.inst-s-r-m-form
       [:div.inst-s-r-m-f-entry
        [:div.inst-s-r-m-f-e-label
         [:span "Email address"]]
        [:span.ui-g-bold email]]
       [step-verify-content-input (:verification-key step)]]
      [:div.inst-s-v-m-result
       (cond
         (:verifying? step) [:i.fa.fa-spinner.fa-spin]
         (= verify-result :ok) (step-verify-result-ok)
         (= verify-result :error) (step-verify-result-error)
         (nil? verify-result) nil)
       [:div.inst-s-v-m-result-msg
        (when-not (nil? (:error-msg step))
          (:error-msg step))]]]]))

(defn view-step-verify []
  (let [account (he/subscribe [:web|install|setup|account])
        step (he/subscribe [:web|install|setup|step|verify])
        current-input-id (:current-input step)
        current-input (get step current-input-id)
        header-title (:header-title step)]
    [render-window
     {:header-title header-title
      :desc-title (:desc-title step)
      :desc-text (:desc-text step)
      :content-hiccup (partial step-verify-content account step)
      :next-enabled? false
      :next-step (:next-step step)
      :prev-step (:prev-step step)}]))

(defn start-verify-polling []
  (js/setInterval #(he/dispatch [:web|install|setup|step|verify|check]) 5000))

(defn prefetch-document-texts []
  (js/setTimeout #(he/dispatch [:web|install|setup|document|fetch :tos]) 500)
  (js/setTimeout #(he/dispatch [:web|install|setup|document|fetch :pp]) 2500))

(defn step-verify-tracker []
  (let [account (he/subscribe [:web|install|setup|account])
        state (r/atom {:timer-ref nil})]
    (when-not (nil? account)
      (r/create-class
       {:reagent-render
        (fn []
          [:div.inst-step-verify-tracker
           {:style {:display :none}}])
        :component-did-mount
        (fn [_]
          (let [timer-ref (start-verify-polling)]
            (swap! state assoc :timer-ref timer-ref)
            (prefetch-document-texts)))
        :component-will-unmount
        (fn [_]
          (let [timer-ref (:timer-ref @state)]
            (when-not (nil? timer-ref)
              (js/clearInterval timer-ref))))}))))


;; Setup > Step > Document (SignTos & SignPP) ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn on-step-document-click
  [document-id radio-id event]
  (he/dispatch [:web|install|setup|document|select document-id radio-id])
  (.stopPropagation event)
  (.preventDefault event))

(defn get-document-name
  [document-id]
  (cond
    (= document-id :tos) "Terms of Service"
    (= document-id :pp) "Privacy Policy"))

(defn render-document-text-empty
  [document-id]
  (js/setTimeout
   #(he/dispatch [:web|install|setup|document|fetch document-id]) 15)
  (when (= document-id :tos)
    (js/setTimeout
     #(he/dispatch [:web|install|setup|document|fetch :pp]) 2500))
  (let [document-name (get-document-name document-id)]
    [:div
     [:span "We are having trouble fetching the "]
     [:span document-name]
     [:span " from our servers."]
     [:br]
     [:span "Click "]
     [:span.inst-m-b-w-c-link
      {:on-click #(he/dispatch [:web|install|setup|document|fetch document-id])}
      "here"]
     [:span " to try fetching again."]
     [:br]
     [:br]
     [:span "If this doesn't work, please "]
     [:span.inst-m-b-w-c-link
      {:on-click #(he/dispatch [:web|install|redirect-login])}
      "go back to the home page"]
     [:span " and try logging in with your username and password."]
     [:span " We apologize for the inconvenience."]]))

(defn render-document-text-html
  [content-html]
  (r/create-class
   {:reagent-render (fn [] [:div])
    :component-did-mount (fn [comp]
                           (let [node (r/dom-node comp)]
                             (set! (.-innerHTML node) content-html)))}))

(defn render-document-text
  [document-id content]
  (if-not (nil? content)
    [render-document-text-html content]
    [render-document-text-empty document-id]))

(defn view-step-sign-document-content
  [document-id step]
  (let [input-text (:input-text step)
        input-selected (:input-selected step)
        accept-checked? (if (= input-selected :accept)
                         true
                         false)
        not-accept-checked? (if (= input-selected :not-accept)
                             true
                             false)
        input-name (str (name document-id) "-input")
        ;; textarea-fn (cond
        ;;               (= document-id :tos) render-tos-text
        ;;               (= document-id :pp) render-pp-text)
        document-name (cond
                        (= document-id :tos) "Terms of Use"
                        (= document-id :pp) "Privacy Policy")]
    [:div.inst-step.inst-step-document
     [:div.inst-s-d-title
      [:span input-text]]
     [:div.inst-s-d-textarea
      (render-document-text document-id (:content step))]
     [:div.inst-s-d-radio
      [:div.inst-s-d-radio-entry
       {:on-click #(on-step-document-click document-id :accept %)}
       [:input
        {:type :radio
         :name input-name
         :checked accept-checked?
         :on-change #()}]
       [:span (str "I have read and I accept the " document-name ".")]]
      [:div.inst-s-d-radio-entry
       {:on-click #(on-step-document-click document-id :not-accept %)}
       [:input
        {:type :radio
         :name input-name
         :checked not-accept-checked?
         :on-change #()}]
       [:span (str "I do not accept the " document-name ".")]]]]))

(defn view-step-sign-document
  [document-id]
  (let [subscription (keyword
                      (str "web|install|setup|step|sign-" (name document-id)))
        step (he/subscribe [subscription])
        content-fun (partial view-step-sign-document-content document-id)]
    [render-window
     {:header-title (:header-title step)
      :desc-title (:desc-title step)
      :desc-text (:desc-text step)
      :content-hiccup (partial content-fun step)
      :next-enabled? false
      :next-step (:next-step step)
      :prev-step (:prev-step step)
      :loading? (:loading? step)}]))

;; Setup > Step > Pricing ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn on-step-pricing-table-help-enter
  [event]
  (let [target (.-nextSibling (.-target event))]
    (.add (.-classList target) "inst-s-p-table-row-item-help-tooltip-visible")))

(defn on-step-pricing-table-help-leave
  [event]
  (let [el (.-nextSibling (.-target event))]
    (.remove (.-classList el) "inst-s-p-table-row-item-help-tooltip-visible")))

(defn step-pricing-table-col-desc []
  [:div.inst-s-p-table-col.inst-s-p-table-col-desc
   [:div.inst-s-p-table-row-logo]
   [:div.inst-s-p-table-row-item.inst-s-p-table-row-item-desc "Single Player"
    [:i.fa.fa-question-circle.inst-s-p-table-row-item-help
     {:on-mouse-enter #(on-step-pricing-table-help-enter %)
      :on-mouse-leave #(on-step-pricing-table-help-leave %)}]
    [:div.inst-s-p-table-row-item-help-tooltip
     "The Single Player mode (also known as Story Mode) is played against the environment, without interacting with other players. Requires an Internet connection."]]
   [:div.inst-s-p-table-row-item.inst-s-p-table-row-item-desc "Multiplayer"
    [:i.fa.fa-question-circle.inst-s-p-table-row-item-help
     {:on-mouse-enter #(on-step-pricing-table-help-enter %)
      :on-mouse-leave #(on-step-pricing-table-help-leave %)}]
    [:div.inst-s-p-table-row-item-help-tooltip
     "The Multiplayer mode is played against the environment and against other players. In this mode players can create complex corporations, share strategies and hack together. It supports PvP and PvE missions. Requires an Internet connection."]]
   [:div.inst-s-p-table-row-item.inst-s-p-table-row-item-desc "Friend Invites"
    [:i.fa.fa-question-circle.inst-s-p-table-row-item-help
     {:on-mouse-enter #(on-step-pricing-table-help-enter %)
      :on-mouse-leave #(on-step-pricing-table-help-leave %)}]
    [:div.inst-s-p-table-row-item-help-tooltip
     "Number of invites you get per plan. Each invite gives the invitee 1 free month of Multiplayer gameplay."]]
   [:div.inst-s-p-table-row-item.inst-s-p-table-row-item-desc "Secret Item"
    [:i.fa.fa-question-circle.inst-s-p-table-row-item-help
     {:on-mouse-enter #(on-step-pricing-table-help-enter %)
      :on-mouse-leave #(on-step-pricing-table-help-leave %)}]
    [:div.inst-s-p-table-row-item-help-tooltip
     "Todo. We are still thinking of a cool bonus to give lifetime players while keeping the gameplay balanced :)."]]

   [:div.inst-s-p-table-row-item.inst-s-p-table-row-item-desc "No microtransactions"
    [:i.fa.fa-question-circle.inst-s-p-table-row-item-help
     {:on-mouse-enter #(on-step-pricing-table-help-enter %)
      :on-mouse-leave #(on-step-pricing-table-help-leave %)}]
    [:div.inst-s-p-table-row-item-help-tooltip
     "We all hate microtransactions. This game is free of them. This game is also free of pay-to-win mechanics."]]

   ])

(def pricing-table-plans
  {:free {:id :free
          :name "Single Player"
          :billed nil
          :price nil
          :item-singleplayer true
          :item-multiplayer false
          :item-invites 0
          :item-advisor false}
   :monthly {:id :monthly
             :name "Monthly"
             :billed "Billed monthly"
             :price-header "3.99"
             :item-singleplayer true
             :item-multiplayer true
             :item-invites 0
             :item-advisor false}
   :yearly {:id :yearly
            :name "Yearly"
            :billed "Billed yearly"
            :price-header "0.99"
            :item-singleplayer true
            :item-multiplayer true
            :item-invites 1
            :item-advisor false}
   :lifetime {:id :lifetime
              :name "Lifetime"
              :billed "Billed once"
              :price-header "19.99"
              :item-singleplayer true
              :item-multiplayer true
              :item-invites 3
              :item-advisor true}})

(defn on-pricing-plan-select
  [plan-id _event]
  (he/dispatch [:web|install|setup|step|pricing|select-plan plan-id]))

(defn step-pricing-table-col-plan
  [plan selected]
  (let [price (:price-header plan)
        price-str (if (nil? price)
                    "Free"
                    (str "$" price))
        frequency (cond
                    (nil? price) "forever!"
                    (= :lifetime (:id plan)) ""
                    :else "per month")
        billed (:billed plan)
        item-singleplayer [:i.fas.fa-check.inst-s-p-table-row-item-ok]
        item-multiplayer (if (:item-multiplayer plan)
                           [:i.fas.fa-check.inst-s-p-table-row-item-ok]
                           "5-day trial")
        item-advisor (if (:item-advisor plan)
                           [:i.fas.fa-check.inst-s-p-table-row-item-ok]
                           [:i.fas.fa-times.inst-s-p-table-row-item-nope])
        item-microtransactions [:i.fas.fa-check.inst-s-p-table-row-item-ok]
        selected? (= selected (:id plan))
        plan-class (if selected?
                         [:inst-s-p-table-col-plan-selected]
                         [])]
    [:div.inst-s-p-table-col.inst-s-p-table-col-plan
     (merge {:class plan-class}
            {:on-click #(on-pricing-plan-select (:id plan) %)})
     [:div.inst-s-p-table-row.inst-s-p-table-row-name
      (:name plan)]
     [:div.inst-s-p-table-row-price
      price-str]
     [:div.inst-s-p-table-row-frequency
      frequency]
     [:div.inst-s-p-table-row-billed
      billed]
     [:div.inst-s-p-table-row-separator]
     [:div.inst-s-p-table-row-item
      item-singleplayer]
     [:div.inst-s-p-table-row-item
      item-multiplayer]
     [:div.inst-s-p-table-row-item
      (:item-invites plan)]
     [:div.inst-s-p-table-row-item
      item-advisor]
     [:div.inst-s-p-table-row-item
      item-microtransactions]]))

(defn step-pricing-content
  [step]
  (let [selected (:selected-plan step)]
    [:div.inst-step.inst-step-pricing
     [:div.inst-s-p-title
      [:span "Hacker Experience has two modes: single player and multiplayer."]
      [:br]
      [:span "The single player mode is free. The multiplayer mode requires a paying subscription."]
      [:br]
      [:span "Please select a plan."]]
     [:div.inst-s-p-table
      [step-pricing-table-col-desc]
      [step-pricing-table-col-plan (:free pricing-table-plans) selected]
      [step-pricing-table-col-plan (:monthly pricing-table-plans) selected]
      [step-pricing-table-col-plan (:yearly pricing-table-plans) selected]
      [step-pricing-table-col-plan (:lifetime pricing-table-plans) selected]]]))

(defn on-step-pricing-already-bought
  [_event]
  (he/dispatch [:web|install|setup|step|pricing|already-bought]))

(defn view-step-pricing []
  (let [step (he/subscribe [:web|install|setup|step|pricing])]
    [render-window
     {:header-title (:header-title step)
      :desc-title (:desc-title step)
      :desc-text (:desc-text step)
      :content-hiccup (partial step-pricing-content step)
      :next-enabled? false
      :next-step (:next-step step)
      :prev-step (:prev-step step)
      :width "700px"
      :height "470px"
      :extra-button-name "I already bought it"
      :extra-button-fn on-step-pricing-already-bought}]))

;; Setup > Step > Finish  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn step-finish-content []
  (let [input-text "Welcome to Hacker Experience!"
        text1 "Your account is set up and you are ready to start your adventure."
        text2 [:div
               [:span "Shall you have any questions, feel free to email us at "]
               [:span.ui-g-bold "contact@hackerexperience.com"]
               [:span " or take a look at "]
               [:span.ui-g-bold "forum.hackerexperience.com"]
               [:span "."]]

        text3 [:span "(Pro tip: you can access the forums with your in-game browser, Icefox.)"]
        text4 [:span "Your journey begins now, happy hacking!"]]
    [:div.inst-step.inst-step-finish
     [:div.inst-s-r-icon
      [:i.fas.fa-user-secret]]
     [:div.inst-s-f-main
      [:div.inst-s-f-m-title input-text]
      [:div.inst-s-f-m-text-entry text1]
      [:br]
      [:div.inst-s-f-m-text-entry text2]
      [:br]
      [:div.inst-s-f-m-text-entry text3]
      [:br]
      [:div.inst-s-f-m-text-entry text4]]]))

(defn view-step-finish []
  (let [step (he/subscribe [:web|install|setup|step|finish])]
    [render-window
     {:header-title (:header-title step)
      :desc-title (:desc-title step)
      :desc-text (:desc-text step)
      :content-hiccup (partial step-finish-content step)
      :next-enabled? true
      :next-step (:next-step step)
      :prev-step (:prev-step step)
      :next-step-str "Finish"}]))

;; Setup ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn view-step-todo []
  [:div "Step todo"])

(defn render-error-window
  [error-window]
  [:div.inst-error-window
   [:div.a-os-err-container
    [:div.a-os-err-header
     [:div.a-os-err-h-title (:title error-window)]
     [:div.a-os-err-h-close
      {:on-click #(he/dispatch (:on-close error-window))}]]
    [:div.a-os-err-body
     [:div.a-os-err-b-fake-icon
      [:i.fas.fa-times-circle]]
     [:div.a-os-err-b-message (:message error-window)]]
    [:div.a-os-err-footer
     [:button
      {:on-click #(he/dispatch (:on-ok error-window))}
      "Ok"]]]])

(defn view-setup-body []
  (let [active-step (he/subscribe [:web|install|setup|step])]
    [:div.inst-main-body
     (match active-step
            :register (do
                        (step-register-focus-username)
                        [view-step-register])
            :verify (do
                      [:<>
                       [step-verify-tracker]
                       [view-step-verify]])
            :sign-tos [view-step-sign-document :tos]
            :sign-pp [view-step-sign-document :pp]
            :pricing [view-step-pricing]
            :finish [view-step-finish])]))

(defn render-setup-sidebar-step
  [step-id step-name current-step]
  (let [step-class (cond
                     (= step-id current-step) "inst-m-sb-step-current"
                     (< step-id current-step) "inst-m-sb-step-completed"
                     :else nil)]
    [:div.inst-m-sb-step
     {:class (when-not (nil? step-class)
               step-class)}
     [:div.inst-m-sb-step-icon]
     [:div.inst-m-sb-step-name step-name]]))

(defn get-setup-step-sidebar-info
  [current-step]
  (match current-step
         :register [1 "2 minutes"]
         :verify [2 "1 minute"]
         :sign-tos [3 "45 seconds"]
         :sign-pp [3 "30 seconds"]
         :pricing [4 "15 seconds"]
         :finish [5 "Finished!"]))

(defn view-setup-sidebar []
  (let [current-step (he/subscribe [:web|install|setup|step])
        [current-id time-left] (get-setup-step-sidebar-info current-step)]
    [:div.inst-main-sidebar
     [:div.inst-m-sb-steps
      [render-setup-sidebar-step 1 "Collecting information" current-id]
      [render-setup-sidebar-step 2 "Verifying email" current-id]
      [render-setup-sidebar-step 3 "Preparing installation" current-id]
      [render-setup-sidebar-step 4 "Installing X.OS" current-id]
      [render-setup-sidebar-step 5 "Finalizing installation" current-id]]
     [:div.inst-m-sb-progress
      [:span.inst-m-sb-progress-desc "Setup will complete in approximately:"]
      [:span.inst-m-sb-progress-time time-left]]]))

(defn view-setup []
  (let [error-window (he/subscribe [:web|install|error-window])]
    [:<>
     [:div.inst
      [:div.inst-header
       [:div.inst-h-logo]]
      [:div.inst-header-separator]
      [:div.inst-main
       [view-setup-sidebar]
       [view-setup-body]]
      [:div.inst-footer-separator]
      [:div.inst-footer
       [:div.inst-f-progress]]]
     (when-not (nil? error-window)
       [render-error-window error-window])]))

(defn view-welcome-main [_]
  [:div.inst-m-b-welcome
   [:div.inst-m-b-welcome-title
    [:div.inst-m-b-welcome-title-icon
     [:i.fab.fa-xing]]
    [:div.inst-m-b-welcome-title-text
     [:span "Welcome to Hacker Experience."]]]
   [:div.inst-m-b-welcome-area
    [:div.inst-m-b-welcome-subtitle
     [:div.inst-m-b-welcome-subtitle-icon
      [:i.far.fa-question-circle]]
     [:div.inst-m-b-welcome-subtitle-text
      [:span "What do you want to do?"]]]
    [:div.inst-m-b-welcome-entry
     {:on-click #(he/dispatch [:web|install|screen :setup])}
     [:div.inst-m-b-welcome-entry-icon.inst-m-b-welcome-entry-icon-main
      [:i.fa.fa-arrow-right]]
     [:div.inst-m-b-welcome-entry-text
      [:span "Create a new account."]]]
    [:div.inst-m-b-welcome-entry
     [:div.inst-m-b-welcome-entry-icon
      [:i.fa.fa-arrow-right]]
     [:div.inst-m-b-welcome-entry-text
      [:span "Recover my password."]]]
    [:div.inst-m-b-welcome-entry
     [:div.inst-m-b-welcome-entry-icon
      [:i.fa.fa-arrow-right]]
     [:div.inst-m-b-welcome-entry-text
      [:span "Check system compatibility."]]]
    [:div.inst-m-b-welcome-entry
     [:div.inst-m-b-welcome-entry-icon
      [:i.fa.fa-arrow-right]]
     [:div.inst-m-b-welcome-entry-text
      [:span "Read about Hacker Experience."]]]]])

(defn view-welcome-else
  [welcome]
  [:div "todo"])

(defn view-welcome-body []
  (let [welcome (he/subscribe [:web|install|welcome])]
    [:div.inst-main-body
     (match (:page welcome)
            :main (view-welcome-main welcome)
            _ (view-welcome-else welcome))]))

(defn view-welcome []
  (let [error-window (he/subscribe [:web|install|error-window])]
    [:<>
     [:div.inst
      [:div.inst-header
       [:div.inst-h-logo]]
      [:div.inst-header-separator]
      [:div.inst-main
       [view-welcome-body]]
      [:div.inst-footer-separator]
      [:div.inst-footer
       [:div.inst-f-progress]]]
     (when-not (nil? error-window)
       [render-error-window error-window])]))

(defn start-timer-transition-boot []
  (js/setTimeout #(he/dispatch [:web|install|screen :boot]) 100))

(defn start-timer-transition-install []
  (js/setTimeout #(he/dispatch [:web|install|screen :welcome]) 200))

(defn view-bsod []
  (start-timer-transition-boot)
  [bsod.view/view])

(defn view-boot []
  (start-timer-transition-install)
  [boot.view/view])

(defn view []
  (let [screen (he/subscribe [:web|install|screen])]
    (match screen
           :bsod (view-bsod)
           :boot (view-boot)
           :welcome (view-welcome)
           :setup (view-setup))))
