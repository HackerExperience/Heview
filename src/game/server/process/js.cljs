(ns game.server.process.js
  (:require [he.dispatch]
            [he.utils.str :as he.str]))

(defn set-var
  [name value]
  (aset js/game.server.process.js name value))

(defn get-var
  [name]
  (aget js/game.server.process.js name))

(defn delete-var
  [name]
  (js-delete js/game.server.process.js name))

;; Timer ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn timer-var-name
  [process-id]
  (str "process-timer-" process-id))

(defn get-timer-var
  [process-id]
  (get-var (timer-var-name process-id)))

(defn format-timer-str
  [time-left]
  (str time-left " seconds"))

(defn process-timer-initial
  [process-id process]
  (let [progress (:progress process)
        creation-date (:creation-date progress)
        completion-date (:completion-date progress)
        time-left (when-not (nil? completion-date)
                    (int (/ (- completion-date creation-date) 1000)))
        ]
    {:time-left time-left
     :time-left-str (format-timer-str time-left)}))

(defn process-timer-iterate
  [{time-left :time-left}]
  (let [new-time-left (dec time-left)]
    {:time-left (dec time-left)
     :time-left-str (format-timer-str new-time-left)}))

(defn process-timer
  [process-id process]
  (let [var-name (timer-var-name process-id)
        prev-timer (get-var var-name)
        next-timer (if (nil? prev-timer)
                     (process-timer-initial process-id process)
                     (process-timer-iterate prev-timer))]
    (set-var var-name next-timer)))

(defn create-process-timer
  [process-id process]
  (process-timer process-id process)
  (js/setInterval #(process-timer process-id process) 1000))

;; Progress ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn progress-var-name
  [process-id]
  (str "process-progress-" process-id))

(defn get-progress-var
  [process-id]
  (get-var (progress-var-name process-id)))

(defn format-progress-str
  [percentage]
  (str (he.str/format "%.1f" percentage) "%"))

(defn process-progress-initial
  [process-id process cur-time]
  (let [progress (:progress process)
        percentage-rate (:percentage-rate progress)
        percentage (* 100 (:percentage progress))]
    {:rate-250ms (/ percentage-rate 4)
     :percentage percentage
     :percentage-str (format-progress-str percentage)}))

(defn process-progress-iterate
  [{rate :rate-250ms percentage :percentage}]
  (let [new-percentage (+ percentage rate)]
    {:rate-250ms rate
     :percentage new-percentage
     :percentage-str (format-progress-str new-percentage)}))

(defn process-progress
  [process-id process cur-time]
  (let [var-name (progress-var-name process-id)
        prev-progress (get-var var-name)
        next-progress (if (nil? prev-progress)
                     (process-progress-initial process-id process cur-time)
                     (process-progress-iterate prev-progress))]
    (set-var var-name next-progress)))

(defn create-process-progress
  [process-id process cur-time]
  (process-progress process-id process cur-time)
  (js/setInterval #(process-progress process-id process cur-time) 250))

;; Ref ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn ref-var-name
  [process-id]
  (str "process-refs-" process-id))

(defn save-ref
  [process-id timer-ref progress-ref]
  (set-var (ref-var-name process-id) (flatten [timer-ref progress-ref])))

(defn destroy-ref
  [process-id]
  (let [refs (get-var (ref-var-name process-id))]
    (doseq [ref-id refs]
      (js/clearInterval ref-id)))
  (delete-var (timer-var-name process-id))
  (delete-var (progress-var-name process-id))
  (delete-var (ref-var-name process-id)))

(defn exists-ref?
  [process-id]
  (not (nil? (get-var (ref-var-name process-id)))))

(defn register-process-timers
  [processes]
  (let [cur-time (.now js/Date)]
    (doseq [[process-id process] processes]
      (when-not (exists-ref? process-id)
        (let [progress-ref (create-process-progress process-id process cur-time)
              timer-ref (create-process-timer process-id process)]
          (save-ref process-id timer-ref progress-ref))))))

(defn deregister-process-timers
  "Destroys existing process timers.
   NOTE: There is a memory leak here. Completed processes won't be on the
  `processes` map, and thus won't be deleted. They must be explicitely deleted
  upon the `process-completed-event` or `process-aborted-event`."
  [processes]
  (doseq [[process-id process] processes]
    (destroy-ref process-id)))
