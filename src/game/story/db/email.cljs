(ns game.story.db.email
  (:require [cljs.core.match :refer-macros [match]]
            [game.story.lang :as l]))

(defn get-meta
  [contact-id email-id meta]
  meta)

(defn prerender
  [contact-id email]
  (let [email-text (l/_ "email" contact-id (:id email) (:meta email))]
    [:span email-text]))

;; (defn email-matcher-friend
;;   [email-id match-fn args]
;;   (match email-id
;;          "welcome" (match-fn args)))

;; (defn email-matcher
;;   [contact-id email-id match-fn args]
;;   (match contact-id
;;          "
;; friend" (email-matcher-friend email-id match-fn args)))
