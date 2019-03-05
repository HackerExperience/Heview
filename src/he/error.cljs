(ns he.error)

;; Internal errors

(defn log-stacktrace []
  ;; (.trace js/console)
  )

(defn dispatch-global-event
  [event detail-data]
  (let [event (new js/CustomEvent event (js-obj "detail" detail-data))]
    (js/setTimeout #(.dispatchEvent js/document event) 100)))

(defn runtime
  ([reason]
   (runtime reason :internal))
  ([reason source]
   (log-stacktrace)
   (dispatch-global-event "os-runtime-error" {:reason reason
                                              :source source})))

(defn match
  [description var]
  (runtime (str "Match error: " description " " var) :match))

(defn not-implemented
  [description]
  (runtime (str "NotImplementedError: " description) :not-implemented))

(defn truss-error
  [{reason :msg_ file :ns-str}]
  (println "Assertion error: " reason)
  (runtime reason :truss))

;; Game errors

(defn base-game-error-messages
  [s]
  (cond
    (= s -1) "Request timed out."
    (= s 0) "Request timed out."
    (= s 400) (str "Bad request. There may be some paramter invalid. "
                   "This is usually our fault, sorry!")
    (= s 403) (str "You are not authorized to perform this action now")
    (<= 500 s 599) (str "Internal server error. This is our fault, sorry!")
    (>= s 600) (str "Request was denied, but the reason wasn't specified. #" s)
    :else (str "Unknown error #" s)))

(defn generic-response-config
  [status text-fn parent-id]
  (let [text-caller (text-fn status)
        text (if (nil? text-caller)
               (base-game-error-messages status)
               text-caller)]
    {:text text
     :title "Oops!"
     :btns [:btn-1]
     :btn-1 {:text "Ok"
             :class [:primary]
             :event [:web|wm|perform [:focus parent-id]]}}))

(defn generic-config
  [text parent-id]
  {:text text
   :title "Oops!"
   :btns [:btn-1]
   :btn-1 {:text "Ok"
           :class [:primary]
           :event [:web|wm|perform [:focus parent-id]]}})
