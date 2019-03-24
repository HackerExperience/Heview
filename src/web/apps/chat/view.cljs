(ns web.apps.chat.view
  (:require [he.core :as he]
            [web.apps.chat.lang :as l]
            [game.story.lang :as story-l]))

(defn- random-key []
  (int (* 100000 (.random js/Math))))

(defn subscribe-chat-entries
  [type contact-id]
  (let [sub (if (= type :story)
              :web|apps|chat|story|entries
              :web|apps|chat|whatsup|entries)]
    (he/subscribe [sub contact-id])))

(defn render-message
  [entry]
  [:div.a-ch-b-mg-m-message
   [:span entry]])

(defn render-group-messages
  [app-id group]
  [:div.a-ch-b-message-group
   [:div.a-ch-b-mg-avatar
    [:div.a-ch-b-mg-avatar-impl]]
   [:div.a-ch-b-mg-main
    [:div.a-ch-b-mg-m-header
     [:div.a-ch-b-mg-m-h-name
      (:name group)]
     [:div.a-ch-b-mg-m-h-timestamp
      "to/do, 2:48pm"]]
    (for [message (:messages group)]
      ^{:key (str app-id "-" (random-key))} [render-message message])]])

(defn render-body
  [app-id type contact-id]
  [:div.a-ch-body
   (let [entries (subscribe-chat-entries type contact-id)]
     (for [group entries]
       ^{:key (str app-id "-" (random-key))}
       [render-group-messages app-id group]))])

(defn on-story-reply-click
  [app-id reply _event]
  (he/dispatch [:web|apps|chat|story|reply app-id reply]))

(defn render-input-area-story-reply
  [app-id contact-id reply]
  [:div.a-ch-ia-reply
   {:on-click #(on-story-reply-click app-id reply %)}
   (story-l/_ "email_reply" contact-id reply)])

(defn render-input-area-story
  [app-id contact-id]
  (let [loading? (he/subscribe [:web|apps|chat|loading? app-id])
        replies (he/subscribe [:web|apps|chat|story|replies contact-id])]
    [:div.a-ch-input-area
     (if-not loading?
       (if-not (empty? replies)
         (for [reply replies]
           ^{:key (str app-id "-" reply)}
           [render-input-area-story-reply app-id contact-id reply])
         [:div.a-ch-ia-empty (l/_ "story-no-replies")])
       [:div.a-ch-ia-loading
        [:i.fa.fa-spinner.fa-spin]])]))

(defn render-input-area-whatsup
  [app-id contact-id]
  [:div.a-ch-input-area
   [:span "Whatsup textarea"]])

(defn render-input-area
  [app-id type contact-id]
  (if (= type :story)
    [render-input-area-story app-id contact-id]
    [render-input-area-whatsup app-id contact-id]))

(defn ^:export view
  [app-id server-cid]
  (let [type (he/subscribe [:web|apps|chat|type app-id])
        contact-id (he/subscribe [:web|apps|chat|contact-id app-id])]
    [:div.a-ch-container
     [render-body app-id type contact-id]
     [render-input-area app-id type contact-id]]))
