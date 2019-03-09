(ns web.home.login.style)

(defn style []
  [[:.home-login-container
    {:color "rgba(155, 243, 255, 0.781)"
     :font-size "11pt"
     :background "rgba(23, 196, 230, 0.1)"
     :display "flex"
     :flex-direction "column"
     :justify-content "center"
     :align-items "center"}]
   [:p {:padding "0.5rem"
        :margin "0.5rem"}]])
