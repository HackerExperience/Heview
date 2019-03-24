(ns game.story.lang.en)

(def ^:export strings
  {
   ;; Full text of each email. Param %3 contains the email metadata.
   "email"
   {"friend"
    {"welcome_c1" #(str "Hey " (:user %3) ", is that you?")
     "welcome_p1" "Maybe. Who's this?"
     "welcome_c2" "Hey, friend! It's me, Kevin."
     "welcome_c3" (str "I heard on the news that you got out of jail today. "
                       "Thought I could reach you on your old email address.")
     "welcome_c4" "How are you?"
     "welcome_p2" (str "I'm okay. I can't wait to start hacking again, and I "
                       "won't stop until I find who framed me.")
     "welcome_c5" "Whaaat? You were framed?"
     "welcome_p3" (str "Of course. Do you really think I'd forget to clear my "
                       "logs? That's such a rookie mistake...")}}

   ;; Small summary of the full response. Shown at the Chat screen
   "email_reply"
   {"friend" {"welcome_p1" "Maybe"
              "welcome_p2" "I'm okay"
              "welcome_p3" "Of course"}}})
