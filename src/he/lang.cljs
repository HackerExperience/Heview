(ns he.lang)

(defn do-translate
  [code result remaining-args all-args]
  (cond
    ;; Result is a string - it's ready, just return it
    (string? result) result

    ;; Result is a map. We'll attempt to match it
    (map? result)
    (let [match-keys (keys result)
          match-arg (first remaining-args)
          match-arg (if (keyword? match-arg)
                      (name match-arg)
                      match-arg)
          match-attempt (get result match-arg)
          new-result (if-not (nil? match-attempt)
                       match-attempt
                       (get result :_))]
      (do-translate code new-result (rest remaining-args) all-args))

    ;; Result is a function. Just recurse it.
    (fn? result) (do-translate
                  code (apply result all-args) (rest remaining-args) all-args)

    ;; Result is nil. It means the given `code` was not found among the strings.
    (nil? result)
    (do
      (println "WARNING: Unhandled string. Code: " code " Args: " all-args)
      (str "Unhandled string: " code))

    ;; Result is something else. That's weird and shouldn't happen.
    :else
    (do
      (println "WARNING: Invalid string. Code: " code " Args: " all-args)
      (str "Invalid string: " result))))

(defn translate
  [context code & args]
  (let [lang-id "en"
        strings (he.dispatch/get-def (str context ".lang." lang-id "/strings"))
        code-str (name code)]
    (do-translate code-str (get strings code-str) args args)))

(defn _
  [context code & args]
  (apply translate context code args))
