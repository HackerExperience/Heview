(ns web.hud.conky.widgets.processes.view
  (:require [reagent.core :as r]
            [he.core :as he]
            [game.server.process.js :as process.js]
            [web.hud.conky.ui :as ckui]))

(defn format-time-left-str
  "This script is prematurely optimized for the most common processes (<10m)"
  [time-left]
  (let [s (mod time-left 60)
        m (mod (int (/ time-left 60)) 60)
        h (mod (int (/ time-left 3600)) 3600)
        d (mod (int (/ time-left 86400)) 86400)]
    (cond
      ;; 59s to 0s
      (> 60 time-left) (str s "s")

      ;; 9m59s to 1m0s
      (> 600 time-left) (let [m (mod (int (/ time-left 60)) 60)]
                          (str m "m" s "s"))

      ;; Anything greater than 10m
      :else (let [m (mod (int (/ time-left 60)) 60)
                  h (mod (int (/ time-left 3600)) 3600)
                  d (mod (int (/ time-left 86400)) 86400)
                  d-str (when (pos? d) (str d "d"))
                  h-str (when (pos? h) (str h "h"))]
              (str d-str h-str m "m")))))


(defn timed-process-progress-fn-pbar
  [[_ element] process-id]
  (let [{percentage :percentage-str} (process.js/get-progress-var process-id)]
    (set! (.-width (.-style element)) percentage)))

(defn timed-process-progress-fn-time-left
  [[element _] process-id]
  (let [{time-left :time-left} (process.js/get-timer-var process-id)]
    (set! (.-innerHTML element) (format-time-left-str time-left))))

(defn timed-process-progress-interval
  [state]
  (let [proc-id (:process-id @state)
        elements (:elements @state)
        ref-time-left (js/setInterval
                       #(timed-process-progress-fn-time-left elements proc-id))
        ref-progress (js/setInterval
                      #(timed-process-progress-fn-pbar elements proc-id)
                      250)]
    (swap! state assoc :refs [ref-time-left ref-progress])))

(defn render-timed-process-entry-progress
  [process-id]
  (let [state (r/atom {:process-id process-id
                       :refs nil
                       :elements nil})]
    (r/create-class
     {:reagent-render
      (fn []
        [:div.hud-ckui-row.hud-ckui-item
         [:div.hud-ckw-prc-timed-pbar
          [:div.hud-ckui-bar
           [:div.hud-ckui-bar-progress]]]
         [:div.hud-ckw-prc-timed-resources;.hud-ckui-pull-right
          [:div.hud-ckw-prc-timed-resource-entry;.hud-ckui-fill-4
           {:style {:min-width "45px"}}
           [:i.far.fa-hourglass.hud-ckw-prc-timed-resource-entry-icon]
           [:span.hud-ckw-prc-timed-time-left "1m"]]
          [:div.hud-ckw-prc-timed-resource-entry;.hud-ckui-fill-4
           [:i.fas.fa-microchip.hud-ckw-prc-timed-resource-entry-icon]
           [:span "100%"]]
          [:div.hud-ckw-prc-timed-resource-entry;..hud-ckui-fill-4
           [:i.fas.fa-memory.hud-ckw-prc-timed-resource-entry-icon]
           [:span "17.1%"]]]]
        )
      :component-did-mount
      (fn [comp]
        (let [process-id (:process-id @state)
              root (r/dom-node comp)
              elements [(.querySelector root ".hud-ckw-prc-timed-time-left")
                        (.querySelector root ".hud-ckui-bar-progress")]]
          (swap! state assoc :elements elements)
          (timed-process-progress-fn-pbar elements process-id)
          (timed-process-progress-fn-time-left elements process-id)

          (js/setTimeout
           #(timed-process-progress-interval state)
           (- 1000 (.getMilliseconds (new js/Date))))
          ))
      :component-will-unmount
      (fn [comp]
        (doseq [ref (:refs @state)]
          (js/clearInterval ref)))})))

(defn render-timed-process-entry
  [entry]
  [:div.hud-ckui-row-hoverable
   [:div.hud-ckui-row.hud-ckui-item
    [:span.hud-ckui-fill-1 (:name entry)]
    [:span.hud-ckui-subitem (:note entry)]]
   [render-timed-process-entry-progress (:process-id entry)]])

(defn render-timed-processes
  [server-cid]
  (let [entries
        (he/sub [:web|hud|conky|widget|processes|entries|timed server-cid])]
    (if-not (empty? entries)
      [:<>
       (println entries)
       (for [entry entries]
         ^{:key (str "hud-ckw-prc-" (:process-id entry))}
         [render-timed-process-entry entry])
       [:div.hud-ckw-prc-separator]]
      [:span "Empty"])))

(defn view []
  (let [server-cid (he/subscribe [:web|wm|active-session])]
    [:div.hud-ckw-prc
     ;; [:div.hud-ckui-row
     ;;  [:span.hud-ckui-label.hud-ckui-fill-1 "Total:"]
     ;;  [:span.hud-ckui-item.hud-ckui-fill-1 "3"]]
     ;; [:div.hud-ckw-prc-separator]

     [render-timed-processes server-cid]


     ]

   ))

   
   ;; [render-timed-processes]

   ;; Timed processes


   ;; [:div.hud-ckui-row-hoverable
   ;;  [:div.hud-ckui-row.hud-ckui-item
   ;;   [:span.hud-ckui-fill-1 "Download file"]
   ;;   [:span.hud-ckui-subitem "Crimsom.crc"]]
   ;;  [:div.hud-ckui-row.hud-ckui-item
   ;;   [:div.hud-ckw-prc-timed-pbar
   ;;    [ckui/progress-bar "40%"]]
   ;;   [:div.hud-ckw-prc-timed-resources;hud-ckui-pull-right
   ;;    [:div.hud-ckw-prc-timed-resource-entry;.hud-ckui-fill-4
   ;;     {:style {:min-width "45px"}}
   ;;     [:i.far.fa-hourglass.hud-ckw-prc-timed-resource-entry-icon]
   ;;     [:span "15m"]]
   ;;    [:div.hud-ckw-prc-timed-resource-entry;.hud-ckui-fill-4
   ;;     [:i.fas.fa-microchip.hud-ckw-prc-timed-resource-entry-icon]
   ;;     [:span "100%"]]
   ;;    [:div.hud-ckw-prc-timed-resource-entry;..hud-ckui-fill-4
   ;;     [:i.fas.fa-memory.hud-ckw-prc-timed-resource-entry-icon]
   ;;     [:span "17.1%"]]

   ;;    ]]]



   ;; [:div.hud-ckw-prc-separator]

   ;; ;; Recursive processes
   ;; [:div.hud-ckui-row
   ;;  [:span.hud-ckui-fill-4 "Process name"]
   ;;  [:span.hud-ckui-fill-3 "Resources"]
   ;;  [:span.hud-ckui-fill-1 "Time"]]

   ;; [:div.hud-ckui-row.hud-ckui-row-hoverable.hud-ckui-item
   ;;  [:span.hud-ckui-fill-4 "Bruteforce server"]
   ;;  [:div.hud-ckui-fill-3
   ;;   [:i.fas.fa-microchip.hud-ckw-prc-recur-resource-icon]
   ;;   [:span.hud-ckw-prc-recur-resource-text "30.1%"]
   ;;   [:i.fas.fa-microchip.hud-ckw-prc-recur-resource-icon]
   ;;   [:span.hud-ckw-prc-recur-resource-text "30.1%"]]
   ;;  [:span.hud-ckui-fill-1 "3h43m"]]


