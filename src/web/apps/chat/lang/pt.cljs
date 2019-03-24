(ns web.apps.chat.lang.pt)

(def ^:export strings
  {"view-profile" "View Profile"
   "weird" []
   "total-messages" {0 "There are no messages"
                     1 "There is one message"
                     :_ #(str "There are " %1 " messages.")}
   "email" {"friend" {"back_thanks" "Yep, I'm back!"
                      "hell_yeah" #(str "Isso eh braziu " %3 " hehehe " %4 ".")}}})
