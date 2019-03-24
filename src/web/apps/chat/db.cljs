(ns web.apps.chat.db)

(def open-opts
  {:len-x 300
   :len-y 350
   :config {:icon-class "fa fa-archive"
            :title "TODO"
            :contextable false
            :show-context false}})

(defn initial
  [type contact-id]
  {:type type
   :contact-id contact-id
   :loading? false})

;; Model

(defn get-contact-id
  [db]
  (:contact-id db))

;; Subs Interface

(defn format-entries-story-reducer-append-message
  [[_ entries] email]
  (let [prev-messages (:messages (last entries))
        new-entry (update (last entries) :messages #(conj % (:hiccup email)))
        new-entries (conj (pop entries) new-entry)]
    [email new-entries]))

(defn format-entries-story-reducer-create-entry
  [player contact [_ entries] email]
  (let [[name avatar] (if (= (:sender email) :contact)
                        [(:name contact) (:avatar contact)]
                        [(:name player) (:avatar player)])]
    [email
     (conj entries
           {:name name
            :avatar avatar
            :timestamp (:timestamp email)
            :messages [(:hiccup email)]})]))

(defn format-entries-story-reducer
  [player contact acc email]
  (let [[prev-email _] acc
        sender-prev-email (:sender prev-email)
        sender-this-email (:sender email)]
    (if (= sender-prev-email sender-this-email)
      (format-entries-story-reducer-append-message acc email)
      (format-entries-story-reducer-create-entry player contact acc email))))

(defn format-entries-story
  [player contact emails]
  (let [reducer (partial format-entries-story-reducer player contact)
        [_ entries] (reduce reducer [nil []] emails)]
    entries))

;; Interface

;; Events API

;; Events API > Requests

(defn story-reply-submit
  [db]
  (assoc db :loading? true))

(defn story-reply-callback
  [app-id]
  {:on-ok [:web|apps|chat|story|reply|ok app-id]
   :on-fail [:web|apps|chat|story|reply|fail app-id]})

(defn story-reply-response-ok
  [db]
  (assoc db :loading? false))

(defn story-reply-response-fail
  [db _status]
  (assoc db :loading? false))

(defn story-reply-fail-config
  [status app-id]
  (he.error/generic-response-config status nil app-id))

;; WM API

(defn ^:export did-open
  [_ctx app-context {arg-type :type arg-contact-id :contact-id}]
  (when (or (nil? arg-contact-id) (nil? arg-type))
    (he.error/runtime "Missing required parameters at chat.db/did-open"))
  [:ok (initial arg-type arg-contact-id) open-opts])

;; Popup handlers
