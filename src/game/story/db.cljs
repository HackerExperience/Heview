(ns game.story.db
  (:require [he.date]
            [game.story.db.email :as story.db.email]))

;; Context

(defn get-context
  [global-db]
  (get-in global-db [:game :story]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:game :story] updated-local-db))

;; Requests

(defn on-reply-sent
  [db contact-id reply-id]
  (let [email {:id reply-id
               :timestamp (.now js/Date)
               :sender :player
               :meta {}}
        email (merge email
                     {:hiccup (story.db.email/prerender contact-id email)})]
    (-> db
        (update-in [:contact contact-id :emails] #(conj % email))
        (assoc-in [:contact contact-id :replies] []))))

;; Bootstrap

(defn format-email-sender
  [sender]
  (if (or (= sender "contact") (= sender :contact))
    :contact
    :player))

(defn build-email
  [contact-id sender raw-email]
  (println raw-email)
  (let [email-id (or (:id raw-email) (:email_id raw-email))
        email {:id email-id
               :timestamp (he.date/timestamp-to-datetime (:timestamp raw-email))
               :sender (format-email-sender sender)
               :meta (story.db.email/get-meta
                      contact-id email-id (:meta raw-email))}]
    (merge email
           {:hiccup (story.db.email/prerender contact-id email)})))

(defn build-emails
  [contact-id raw-emails]
  (into []
        (map (fn [raw-email]
               (build-email contact-id (:sender raw-email) raw-email))
             raw-emails)))

(defn on-email-sent
  [db raw-email]
  (let [contact-id (keyword (:contact_id raw-email))
        email (build-email contact-id :contact raw-email)]
    (println "Go go go " email)
    (-> db
        (update-in [:contact contact-id :emails] #(conj % email))
        (assoc-in [:contact contact-id :replies] (:replies raw-email)))))

(defn bootstrap-account-contact-reducer
  [acc [contact-id contact-data]]
  (let [entry {:name "Kevin"
               :avatar "todo"
               :online? (or (:online? contact-data) true)
               :replies (:replies contact-data)
               :emails (build-emails contact-id (:emails contact-data))}]
    (assoc acc contact-id entry)))

(defn bootstrap-account
  [data]
  (let [contacts (reduce bootstrap-account-contact-reducer {} data)]
    {:contact contacts}))
