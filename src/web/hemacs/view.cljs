(ns web.hemacs.view
  (:require [cljs.core.match :refer-macros [match]]
            [he.core :as he]))

(defn recalculate-mode-callback
  [wm-overview]
  (he/dispatch [:web|hemacs|find-mode wm-overview]))

(defn hemacs-mode-tracker []
  (let [focused-app-id (he/subscribe [:web|wm|focused-window])
        active-session (he/subscribe [:web|wm|active-session])]
    (recalculate-mode-callback
     {:focused-app focused-app-id
      :session-id active-session})
    [:div.hemacs-mode-tracker
     {:display :none}]))

(defn render-minibuffer-keybuffer []
  (let [buffer (he/subscribe [:web|hemacs|buffer])
        last-buffer (he/subscribe [:web|hemacs|last-buffer])
        display-buffer (if (empty? buffer)
                         last-buffer
                         buffer)]
    [:div.hemacs-minibuffer-keybuffer
     (for [key display-buffer]
       ^{:key (int (* 10000 (.random js/Math)))} [:span key])]))

(defn render-minibuffer-output-nomatch
  [reason]
  [:div.hemacs-minibuffer-output
   [:span.output-error reason]])

(defn render-minibuffer-output-exact-match
  []
  [:div.hemacs-minibuffer-output
   [:span "OK"]])

(defn render-minibuffer-output []
  (let [output (he/subscribe [:web|hemacs|output])]
    (match output
           [:no-match reason] [render-minibuffer-output-nomatch reason]
           [:exact-match] [render-minibuffer-output-exact-match]
           [:multiple-match] nil
           nil nil)))

(defn render-minibuffer []
  (let [mode-name (he/subscribe [:web|hemacs|mode|name])]
    [:div#hemacs-minibuffer
     [:div.hemacs-minibuffer-mode mode-name]
     [:div.hemacs-minibuffer-separator]
     [render-minibuffer-keybuffer]
     [:div.hemacs-minibuffer-separator]
     [render-minibuffer-output]
     ]))

(defn render-which-key-entry
  [key description disabled?]
  [:div.hemacs-which-key-entry
   {:class (when disabled? :entry-disabled)}
   [:span.entry-key key]
   [:span.entry-separator "-"]
   [:span.entry-description description]])

(defn render-which-key []
  (let [{which-disabled :disabled
         which-enabled :enabled} (he/subscribe [:web|hemacs|which-key])]
    (when-not (and
               (nil? which-enabled)
               (nil? which-disabled))
      [:div#hemacs-which-key
       [:div.hemacs-which-key-grid
        (for [[key desc] which-enabled]
          ^{:key (str "which-" key)} [render-which-key-entry key desc false])
        (for [[key desc] which-disabled]
          ^{:key (str "which-" key)} [render-which-key-entry key desc true])]])))

(defn render-hemacs []
  [:div#hemacs
   [hemacs-mode-tracker]
   [render-minibuffer]
   [render-which-key]])

(defn view []
  (let [enabled? (he/subscribe [:web|hemacs|enabled?])]
    (when enabled?
      [render-hemacs])))
