(ns web.wm.db
  (:require [cljs.core.match :refer-macros [match]]
            [web.apps.db :as apps.db]
            [web.wm.validators :as v]))

;; Context ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def v-db
  (partial v/v v/db))

(defn get-context
  [global-db]
  (get-in global-db [:web :wm]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:web :wm] (v-db updated-local-db)))

;; Model ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-session
  [db session-id]
  (get-in db [:sessions session-id]))

(defn get-active-session
  [db]
  (get-in db [:active-session]))

(defn get-active-session-context
  [db]
  (if (.test #"@" (get-active-session db))
    :remote
    :local))

(defn set-active-session
  [db server-cid]
  (assoc db :active-session server-cid))

(defn get-window-data
  [db app-id]
  (get-in db [:windows app-id]))

(defn get-next-open-position
  [db]
  (get-in db [:next-open]))

(defn get-next-z-index
  [db]
  (get-in db [:next-z-index]))

(defn increment-next-z-index
  [db]
  (update-in db [:next-z-index] inc))

(defn increment-next-open-position
  "Increments next default open position. If the app supplied its own open
  position (common with popups), this operation is ignored. "
  [db opts]
  (if-not (and
           (contains? opts :x)
           (contains? opts :y))
    (let [{current-open-x :x
           current-open-y :y} (get-next-open-position db)
          {next-open-x :x
           next-open-y :y} (cond
                             (>= current-open-x 1280) {:x 150 :y 150}
                             (>= current-open-y 500) {:x (+ current-open-x 90)
                                                      :y 150}
                             :else {:x (+ current-open-x 20)
                                    :y (+ current-open-y 40)})]
      (assoc-in db [:next-open] {:x next-open-x :y next-open-y}))
    db))

(defn get-visible-apps
  [db session-id]
  (:visible-apps (get-session db session-id)))

(defn get-next-seq-id
  [db]
  (:next-seq-id db))

(defn increment-next-seq-id
  [db]
  (update db :next-seq-id inc))

(defn reset-next-seq-id
  [db]
  (assoc db :next-seq-id 1))

(defn recalculate-window-seq-ids-reducer
  [db app-id]
  (let [next-seq-id (get-next-seq-id db)]
    (-> db
        (assoc-in [:windows app-id :seq-id] next-seq-id)
        (increment-next-seq-id))))

(defn recalculate-window-seq-ids
  [db session-id]
  (let [visible-apps (get-visible-apps db session-id)]
    (reduce recalculate-window-seq-ids-reducer db visible-apps)))

;; WM

(defn get-viewport
  [db]
  (get-in db [:viewport]))

(defn recalculate-viewport
  [db]
  (let [viewport-x (.-clientWidth (.-documentElement js/document))
        viewport-y (.-clientHeight (.-documentElement js/document))]
    (assoc-in db [:viewport] {:x viewport-x
                              :y viewport-y})))

(defn get-server-cid
  [db context]
  (let [session-id (get-active-session db)
        session (get-session db session-id)]
    (if (= context :local)
      (:gateway session)
      (:endpoint session))))

(defn get-other-server-cid
  [db context]
  (let [session-id (get-active-session db)
        session (get-session db session-id)]
    (if (= context :remote)
      (:gateway session)
      (:endpoint session))))

(defn initial-session-gateway-state
  [gateway-cid]
  {:gateway gateway-cid
   :endpoint nil
   :apps []})

(defn initial-session-endpoint-state
  [gateway-cid endpoint-cid]
  {:gateway gateway-cid
   :endpoint endpoint-cid
   :apps []})

(defn set-session-endpoint
  [db session-id endpoint-cid]
  (assoc-in db [:sessions session-id :endpoint] endpoint-cid))

(defn create-endpoint-session
  [db gateway-cid endpoint-cid]
  (assoc-in
   db
   [:sessions endpoint-cid]
   (initial-session-endpoint-state gateway-cid endpoint-cid)))

;; WM > Window Movement ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-window-moving
  [db]
  (get-in db [:window-moving?]))

; TODO
(defn get-window-size
  [db app-id]
  (let [{{x :x y :y} :length} (get-window-data db app-id)]
    {:x x :y y}))

(defn start-moving-window
  [db app-id click-position]
  (-> db
      (assoc-in [:windows app-id :moving?] true)
      (assoc-in [:window-moving?] app-id)
      (assoc-in [:windows app-id :drag-click] click-position)))

(defn stop-moving-window
  [db app-id]
  (-> db
      (assoc-in [:window-moving?] nil)
      (assoc-in [:windows app-id :moving?] false)))

(defn calculate-window-position
  [db app-id move-x move-y]
  (let [{{click-x :x click-y :y} :drag-click
         {source-x :x source-y :y} :position} (get-window-data db app-id)]
    {:x (- source-x (- click-x move-x))
     :y (- source-y (- click-y move-y))}))

(defn apply-window-boundaries
  [db app-id {x :x y :y}]
  (let [{viewport-x :x viewport-y :y} (get-viewport db)
        {window-size-x :x window-size-y :y} (get-window-size db app-id)
        header-length 90
        footer-length 43
        screen-max-x (- viewport-x window-size-x 1)
        screen-max-y (- viewport-y window-size-y footer-length)
        screen-max-x (if (neg? screen-max-x) 1 screen-max-x)
        screen-max-y (if (neg? screen-max-y) header-length screen-max-y)]
    {:x (cond
          (< x 1) 1
          (> x screen-max-x) screen-max-x
          :else x)
     :y (cond
          (< y header-length) header-length
          (> y screen-max-y) screen-max-y
          :else y)}))

(defn update-window-position
  [db app-id move-x move-y]
  (let [base-position (calculate-window-position db app-id move-x move-y)
        new-position (apply-window-boundaries db app-id base-position)
        drag-position (apply-window-boundaries db app-id {:x move-x :y move-y})]
    (-> db
        (assoc-in [:windows app-id :drag-click] drag-position)
        (assoc-in [:windows app-id :position] new-position))))

;; WM > Window Focus ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-focused-window
  [db]
  (get-in db [:focused-window]))

(defn unfocus-currently-focused-window
  [db]
  (let [currently-focused-window (get-focused-window db)]
    (if-not (nil? currently-focused-window)
      (assoc-in db [:windows currently-focused-window :focused?] false)
      db)))

(defn unfocus-on-close
  [db app-id]
  (let [currently-focused-window (get-focused-window db)]
    (if (= currently-focused-window app-id)
      (assoc-in db [:focused-window] nil)
      db)))

(defn set-focused-window
  [db app-id]
  (-> db
      (unfocus-currently-focused-window)
      (assoc-in [:windows app-id :focused?] true)
      (assoc-in [:focused-window] app-id)))

(defn window-focused?
  [db app-id]
  (= app-id (get-focused-window db)))

(defn focus-window
  [db app-id]
  (-> db
      (assoc-in [:windows app-id :z-index] (get-next-z-index db))
      (set-focused-window app-id)
      (increment-next-z-index)))

;; WM > Window resize ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn resize-window
  [db app-id new-size]
  (assoc-in db [:windows app-id :length] new-size))

(defn retitle-window
  [db app-id new-title]
  (assoc-in db [:windows app-id :config :title] new-title))

;; WM > Window file ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn update-window-file
  [db app-id new-file-id]
  (assoc-in db [:windows app-id :file-id] new-file-id))

;; App ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; App > Open ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-window-length
  [opts]
  (let [len-x (get opts :len-x 530)
        len-y (get opts :len-y 400)]
    {:len-x len-x
     :len-y len-y}))

(defn get-open-position
  [db opts]
  (if (and
       (contains? opts :x)
       (contains? opts :y))
    {:x (:x opts)
     :y (:y opts)}
    (get-next-open-position db)))

(defn extract-window-config
  [opts]
  {:full-view (get opts :full-view false)
   :title (get opts :title "Window title")
   :icon-class (get opts :icon-class "fab fa-windows")
   :show-taskbar (get opts :show-taskbar true)
   :show-context (get opts :show-context true)
   :show-minimize (get opts :show-minimize true)
   :show-close (get opts :show-close true)
   :contextable (get opts :contextable true)

   ;; This doesn't belong here, but I'm leaving here for "performance" reasons
   ;; (to not create yet another n subscriptions on `web|hud|taskbar|entries`).
   ;; Move someplace else if you (me) ever need this data on another context.
   :open-timestamp (.now js/Date)})

(defn initial-window-data
  [db opts]
  (let [next-z-index (get-next-z-index db)
        {open-x :x open-y :y} (get-open-position db opts)
        {len-x :len-x len-y :len-y} (get-window-length opts)]
    {:moving? false
     :position {:x open-x :y open-y}
     :length {:x len-x :y len-y}
     :z-index next-z-index
     :config (extract-window-config (:config opts))
     :seq-id (get-next-seq-id db)
     :file-id (:file-id opts)
     :file-type (:file-type opts)}))

(defn add-window-data
  [db app-id opts]
  (assoc-in db [:windows app-id] (initial-window-data db opts)))

(defn add-app-entry
  [db session-id app-id]
  (-> db
      (update-in [:sessions session-id :visible-apps] #(vec (conj % app-id)))
      (update-in [:sessions session-id :apps] #(vec (conj % app-id)))))

(defn on-open
  [db app-id session-id opts]
  (-> db
      (add-window-data app-id opts)
      (add-app-entry session-id app-id)
      (set-focused-window app-id)
      (increment-next-seq-id)
      (increment-next-z-index)
      (increment-next-open-position opts)))

;; App > Close ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn remove-app-entry
  [db session-id app-id]
  (-> db
      (assoc-in
       [:sessions session-id :visible-apps]
       (remove #(= app-id %) (get-in db [:sessions session-id :visible-apps])))
      (assoc-in
       [:sessions session-id :apps]
       (remove #(= app-id %) (get-in db [:sessions session-id :apps])))))

(defn on-close
  [db app-id]
  (let [session-id (get-active-session db)]
    (-> db
        (he.utils/dissoc-in [:windows app-id])
        (unfocus-on-close app-id)
        (remove-app-entry session-id app-id)
        (reset-next-seq-id)
        (recalculate-window-seq-ids session-id))))

;; Bootstrap ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn reducer-init-local-sessions
  [acc-db gateway-id]
  (assoc-in
   acc-db
   [:sessions gateway-id]
   (initial-session-gateway-state gateway-id)))

(defn init-local-sessions
  [db gateways]
  (reduce reducer-init-local-sessions db gateways))

(defn init-wm
  [db]
  (-> db
      (assoc-in [:next-seq-id] 1)
      (assoc-in [:next-z-index] 100)
      (assoc-in [:next-open] {:x 150 :y 150})
      (assoc-in [:focused-window] nil)
      (assoc-in [:window-moving?] nil)
      (assoc-in [:windows] {})
      (recalculate-viewport)))

(defn bootstrap
  [db gateways mainframe-id]
  (-> db
      (init-local-sessions gateways)
      (init-wm)
      (assoc-in [:active-session] mainframe-id)))

;; Query ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn query
  [db app-id]
  (get-window-data db app-id))

(defn- apply-viewport-boundary-x
  [raw-x child-len-x viewport-x]
  (let [real-length-x (+ raw-x child-len-x)]
    (cond
      (<= raw-x 0) (.abs js/Math raw-x)
      (>= real-length-x viewport-x) (let [diff (- real-length-x viewport-x)]
                                      (- raw-x (* diff 2)))
      :else raw-x)))

(defn- apply-viewport-boundary-y
  [raw-y child-len-y viewport-y]
  (let [real-length-y (+ raw-y child-len-y)]
    (cond
      (<= raw-y 0) (.abs js/Math raw-y)
      (>= real-length-y viewport-y) (let [diff (- real-length-y viewport-y)]
                                      (- raw-y (* diff 2)))
      :else raw-y)))

(defn calculate-next-position-x
  [{pad-x :x} {len-x :x} {child-len-x :len-x} {viewport-x :x}]
  (let [x-rate (float (/ child-len-x len-x))
        x-spacing (- 1 x-rate)
        x-spacing-width (* x-spacing len-x)
        x-spacing-half (int (/ x-spacing-width 2))
        raw-new-x (+ pad-x x-spacing-half)
        bounded-x (apply-viewport-boundary-x raw-new-x child-len-x viewport-x)]
    (if (pos? bounded-x)
      bounded-x
      1)))

(defn calculate-next-position-y
  [{pad-y :y} {len-y :y} {child-len-y :len-y} {viewport-y :y}]
  (let [y-spacing (int (* 0.3 len-y))
        raw-new-y (if (and
                       (>= pad-y (int (/ viewport-y 2)))
                       (>= child-len-y len-y))
                    (- pad-y y-spacing)
                    (+ pad-y y-spacing))
        bounded-y (apply-viewport-boundary-y raw-new-y child-len-y viewport-y)]
    (if (pos? bounded-y)
      bounded-y
      90)))

(defn query-calculate-next-position-popup
  [db parent-window child-length]
  (let [{padding :position length :length} parent-window
        viewport (get-viewport db)]
    {:x (calculate-next-position-x padding length child-length viewport)
     :y (calculate-next-position-y padding length child-length viewport)}))

(defn query-calculate-next-position
  [db app-window length method]
  (match [method]
         [:popup] (query-calculate-next-position-popup db app-window length)
         [_] {:x 50 :y 50}))
