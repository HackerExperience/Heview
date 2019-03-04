(ns web.wm.handlers
  (:require [cljs.core.match :refer-macros [match]]
            [he.core :as he]
            [game.server.db :as server.db]
            [web.wm.db :as wm.db]
            [web.apps.db :as apps.db]
            [web.wm.windowable :as wm.windowable]))

;; Utils ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- ctx
  [gdb]
  (let [game-db (get-in gdb [:game]) ;; todo
        app-db (apps.db/get-context gdb)
        wm-db (wm.db/get-context gdb)]
    {:game game-db
     :app app-db
     :wm wm-db}))

(defn- wrap-perform
  [result]
  [:web|wm|perform result])

(defn- dispatch-perform
  [result]
  {:dispatch (wrap-perform result)})

(defn- ignore-with-warning
  [gdb warning-atom]
  (println warning-atom)
  {:gdb gdb})

;; Bootstrap ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-db
 :web|wm|bootstrap
 (fn [gdb _]
   (let [server-db (server.db/get-context gdb)
         gateways (server.db/get-gateways-ids server-db)
         mainframe (server.db/get-mainframe server-db)]
     (as-> {} ldb
       (wm.db/bootstrap ldb gateways mainframe)
       (wm.db/set-context gdb ldb)))))

;; App ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-db
 :web|wm|app|switch-context
 (fn [gdb [_ app-id]]
   (let [wm-db (wm.db/get-context gdb)
         app-db (apps.db/get-context gdb)
         app-context (apps.db/get-app-context app-db app-id)
         other-context-cid (wm.db/get-other-server-cid wm-db app-context)
         _ (when (nil? other-context-cid)
             (he.error/runtime "Shouldn't be able to switch context"))
         new-app-db (apps.db/switch-context app-db app-id other-context-cid)]
     (apps.db/set-context gdb new-app-db))))

;; WM ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-db
 :web|wm|viewport-resized
 (fn [gdb _]
   (as-> (wm.db/get-context gdb) ldb
     (wm.db/recalculate-viewport ldb)
     (wm.db/set-context gdb ldb))))

;; WM > Windows ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-db
 :web|wm|window|resize
 (fn [gdb [_ app-id new-size]]
   (as-> (wm.db/get-context gdb) ldb
     (wm.db/resize-window ldb app-id new-size)
     (wm.db/set-context gdb ldb))))

(he/reg-event-db
 :web|wm|window|retitle
 (fn [gdb [_ app-id new-title]]
   (as-> (wm.db/get-context gdb) ldb
     (wm.db/retitle-window ldb app-id new-title)
     (wm.db/set-context gdb ldb))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WM API ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; WM API > Open ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-fx
 :web|wm|app|open
 (fn [{gdb :db} [_ app-type app-context]]
   (let [app-context (if (nil? app-context)
                       (as-> (wm.db/get-context gdb) ldb
                         (wm.db/get-active-session-context ldb))
                       app-context)]
     (dispatch-perform
      (wm.windowable/will-open app-type (ctx gdb) app-context)))))

(defn- proceed-open-popup
  [ctx [app-type popup-type parent-id args xargs]]
  (dispatch-perform
   (wm.windowable/popup-will-open
    app-type popup-type ctx parent-id args xargs)))

(he/reg-event-fx
 :web|wm|app|open-popup
 (fn [{gdb :db} [_ [app-type popup-type parent-id] args xargs]]
   (let [ctx (ctx gdb)]
     (match (wm.windowable/popup-may-open
             app-type popup-type ctx parent-id args xargs)
            [:open-popup & rest] (proceed-open-popup ctx rest)
            other-action (dispatch-perform other-action)))))

;; WM API > Close ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- proceed-close-popup
  [ctx state [app-type popup-type family-ids args xargs]]
  (wm.windowable/popup-will-close
   app-type popup-type ctx family-ids state args xargs))

(defn- close-windowable
  [ctx app-id state type popup]
  (if (= type :popup)
    (let [{app-type :app-type
           popup-type :popup-type
           parent-id :parent-id} popup]
      (match (wm.windowable/popup-may-close
              app-type popup-type ctx [parent-id app-id] state [])
             [:close-popup & args] (proceed-close-popup ctx state args)
             other-action other-action))
    (wm.windowable/will-close type ctx app-id state)))

(he/reg-event-fx
 :web|wm|app|close
 (fn [{gdb :db} [_ app-id]]
   (let [ldb-app (apps.db/get-context gdb)
         app (apps.db/fetch ldb-app app-id)
         state (apps.db/get-state app)
         {type :type popup :popup} (apps.db/get-meta app)]
     (dispatch-perform (close-windowable (ctx gdb) app-id state type popup)))))

;; WM API > Header Control ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-db
 :web|wm|app|on-header-click
 (fn [gdb [_ app-id click-position]]
   (as-> (wm.db/get-context gdb) ldb
     (wm.db/start-moving-window ldb app-id click-position)
     (wm.db/set-context gdb ldb))))

(he/reg-event-db
 :web|wm|app|on-header-unclick
 (fn [gdb _]
   (let [ldb (wm.db/get-context gdb)
         app-id (wm.db/get-window-moving ldb)]
     (if-not (nil? app-id)
       (as-> ldb ldb
         (wm.db/stop-moving-window ldb app-id)
         (wm.db/set-context gdb ldb))
       gdb))))

(he/reg-event-db
 :web|wm|app|on-header-move
 (fn [gdb [_ position]]
   (let [ldb (wm.db/get-context gdb)
         app-id (wm.db/get-window-moving ldb)]
     ;; Sometimes `on-header-move` is emitted around the same time
     ;; `on-header-unclick`, leading to a possible race condition here
     (if-not (nil? app-id)
       (as-> ldb ldb
         (wm.db/update-window-position ldb app-id (:x position) (:y position))
         (wm.db/set-context gdb ldb))
       gdb))))

;;

(defn proceed-focus-popup
  [ctx state [app-type popup-type family-ids args xargs]]
  (wm.windowable/popup-will-focus
   app-type popup-type ctx family-ids state args xargs))

(defn focus-windowable
  [ctx app-id state type popup]
  (if (= type :popup)
    (let [{app-type :app-type
           popup-type :popup-type
           parent-id :parent-id} popup]
      (match (wm.windowable/popup-may-focus
              app-type popup-type ctx [parent-id app-id] state [])
             [:focus-popup & rest] (proceed-focus-popup ctx state rest)
             other-action other-action))
     (wm.windowable/will-focus type ctx app-id state)))

(he/reg-event-fx
 :web|wm|window|focus
 (fn [{gdb :db} [_ app-id]]
   (let [ldb-app (apps.db/get-context gdb)
         app (apps.db/fetch ldb-app app-id)
         state (apps.db/get-state app)
         {type :type popup :popup} (apps.db/get-meta app)]
     (dispatch-perform (focus-windowable (ctx gdb) app-id state type popup)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WM > Perform ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; WM > Perform > Open ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- on-open-ok
  [gdb app-type state opts popup-info app-context]
  (let [ldb-app (apps.db/get-context gdb)
        ldb-wm (wm.db/get-context gdb)

        app-id (str (random-uuid)) ;; todo: co-fx
        session-id (wm.db/get-active-session ldb-wm)
        server-cid (wm.db/get-server-cid ldb-wm app-context)

        new-ldb-wm (wm.db/on-open ldb-wm app-id session-id opts)
        new-ldb-app (apps.db/on-open
                     ldb-app app-id session-id server-cid app-type app-context
                     state popup-info)

        new-gdb (-> gdb
                    (wm.db/set-context new-ldb-wm)
                    (apps.db/set-context new-ldb-app))]
    {:db new-gdb}))

(defn- perform-open-app
  [gdb [app-type app-context]]
  (match (wm.windowable/did-open app-type (ctx gdb) app-context)
         [:ok state opts] (on-open-ok gdb app-type state opts nil app-context)
         [:error reason] {:db gdb
                          :dispatch [:web|wm|app|open-failed reason]}))

(defn- perform-open-popup
  [gdb [app-type popup-type parent-id args xargs]]
  (match (wm.windowable/popup-did-open
          app-type popup-type (ctx gdb) parent-id args xargs)
         [:ok state opts]
         (let [popup-info [popup-type parent-id]
               ;; Popups always open within the same context as their parents
               parent-context (as-> (apps.db/get-context gdb) app-db
                                (apps.db/get-app-context app-db parent-id))]
           (on-open-ok gdb app-type state opts popup-info parent-context))
         [:error reason] {:db gdb
                          :dispatch [:web|wm|app|open-failed reason]}))

;; WM > Perform > Close ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- on-close-ok
  [gdb ldb-app app-id]
  (let [ldb-wm (wm.db/get-context gdb)
        new-ldb-app (apps.db/on-close ldb-app app-id)
        new-ldb-wm (wm.db/on-close ldb-wm app-id)
        new-gdb (-> gdb
                    (wm.db/set-context new-ldb-wm)
                    (apps.db/set-context new-ldb-app))]
    {:db new-gdb}))

(defn- windowable-close-popup
  [ctx app-id popup state xargs]
  (let [{parent-id :parent-id
         app-type :app-type
         popup-type :popup-type} popup]
    (wm.windowable/popup-did-close
     app-type popup-type ctx [parent-id app-id] state [] xargs)))

(defn- do-perform-close-app
  [gdb ldb-app app app-id xargs]
  (let [ctx (ctx gdb)
        state (apps.db/get-state app)
        {type :type popup :popup} (apps.db/get-meta app)
        windowable-close (if (= type :popup)
                           (windowable-close-popup ctx app-id popup state xargs)
                           (wm.windowable/did-close type ctx app-id state xargs))]
    (match windowable-close
           [:ok] (on-close-ok gdb ldb-app app-id)
           [:error reason] {:db gdb
                            :dispatch [:web|wm|app|close-failed reason]})))

(defn perform-close-app
  [gdb [app-id xargs]]
  (let [ldb-app (apps.db/get-context gdb)
        app (apps.db/fetch ldb-app app-id)]
    (if-not (nil? app)
      (do-perform-close-app gdb ldb-app app app-id xargs)
      (ignore-with-warning gdb :attempted-close-missing))))

(defn- perform-close-popup
  [gdb [_ _ [-parent-id app-id] _args xargs]]
  (perform-close-app gdb [app-id xargs]))

;; WM > Perform > Focus ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TODO: Timing error here. Sometimes, if you quickly focus a window that you
;; just closed, this will execute after the window has been closed, and truss
;; will complain (correctly). If this happens again, add a check that the
;; window hasn't closed.
(defn- perform-focus
  [gdb [app-id _xargs]]
  (let [ldb (wm.db/get-context gdb)]
    {:db (if-not (wm.db/window-focused? ldb app-id)
           (as-> ldb ldb
             (wm.db/focus-window ldb app-id)
             (wm.db/set-context gdb ldb))
           gdb)}))

;; WM > Perform > Vibrate Focus ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TODO: Move to an "animation bus" at the OS level
(defn js-vibrate-add
  [app-id interval]
  (js/setTimeout
   #(let [element (.getElementById js/document app-id)]
      (.add (.-classList element) "app-vibrate"))
   interval))

;; TODO: Move to an "animation bus" at the OS level
(defn js-vibrate-remove
  [app-id interval]
  (js/setTimeout
   #(let [element (.getElementById js/document app-id)]
      (.remove (.-classList element) "app-vibrate"))
   interval))

(defn- perform-vibrate-focus
  [gdb [app-id xargs]]
  ;; TODO: Move to an "animation bus" at the OS level
  (js-vibrate-add app-id 150)
  (js-vibrate-remove app-id 250)
  (js-vibrate-add app-id 400)
  (js-vibrate-remove app-id 500)
  (let [subfocus-target (:subfocus xargs)
        subfocus-event (when-not (nil? subfocus-target)
                         (wrap-perform [:focus subfocus-target]))]
    {:dispatch-n (list
                  subfocus-event
                  (wrap-perform [:focus app-id]))}))

;; WM > Perform > Confirm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- perform-confirm
  [gdb [parent-id config xargs]]
  (dispatch-perform
   [:dispatch [:web|wm|app|open-popup [:os :confirm parent-id] [config] xargs]]))

;; WM > Perform > Misc ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- perform-dispatch
  [gdb [event]]
  {:dispatch event})

(defn- perform-noop
  [gdb]
  {:db gdb})

;; WM > Perform > Handler ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-fx
 :web|wm|perform
 (fn [{gdb :db} [_ perform-args]]
   (match perform-args
          [:open-app & args] (perform-open-app gdb args)
          [:open-popup & args] (perform-open-popup gdb args)
          [:close-app & args] (perform-close-app gdb args)
          [:close-popup & args] (perform-close-popup gdb args)
          [:focus & args] (perform-focus gdb args)
          [:vibrate-focus & args] (perform-vibrate-focus gdb args)
          [:confirm & args] (perform-confirm gdb args)
          [:dispatch & args] (perform-dispatch gdb args)
          [:noop] (perform-noop gdb)
          else (he.error/runtime (str "Unknown perform WM action: " else)))))

;; WM > Sessions ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(he/reg-event-db
 :web|wm|on-remote-login
 (fn [gdb [_ gateway-cid remote-cid]]
   (as-> (wm.db/get-context gdb) ldb
     (wm.db/create-endpoint-session ldb gateway-cid remote-cid)
     (wm.db/set-session-endpoint ldb gateway-cid remote-cid)
     (wm.db/set-context gdb ldb))))

(he/reg-event-db
 :web|wm|set-active-session
 (fn [gdb [_ server-cid]]
   (as-> (wm.db/get-context gdb) ldb
     (wm.db/set-active-session ldb server-cid)
     (wm.db/set-context gdb ldb))))
