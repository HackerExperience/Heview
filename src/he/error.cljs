(ns he.error)

(defn log-stacktrace []
  (.trace js/console))

(defn dispatch-global-event
  [event detail-data]
  (let [event (new js/CustomEvent event (js-obj "detail" detail-data))]
    (js/setTimeout #(.dispatchEvent js/document event) 100)))

(defn throw-runtime-error
  ([reason]
   (throw-runtime-error reason :internal))
  ([reason source]
   (log-stacktrace)
   (dispatch-global-event "os-runtime-error" {:reason reason
                                              :source source})))
