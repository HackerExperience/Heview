(ns web.apps.chat.lang.en)

(def ^:export strings
  {"story-no-replies" "You have nothing to say for now."
   "total-messages" {0 "There are no messages"
                     1 "There is one message"
                     :_ #(str "There are " %1 " messages.")}})
