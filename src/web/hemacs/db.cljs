(ns web.hemacs.db
  (:require [cljs.core.match :refer-macros [match]]
            [he.core :as he]
            [web.hemacs.dispatcher :as hemacs.dispatcher]
            [web.hemacs.utils :as hemacs.utils]))

;; Context ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-context
  [global-db]
  (get-in global-db [:web :hemacs]))

(defn set-context
  [global-db updated-local-db]
  (assoc-in global-db [:web :hemacs] updated-local-db))

;; Dispatcher ;;

(defn dispatch-app
  ([app-type method]
   (dispatch-app app-type method []))
  ([app-type method args]
   (hemacs.dispatcher/dispatch [:app app-type] method args)))

;; Gambiarra ;;

(defn erase-markers
  [db]
  (when (:with-markers? db)
    (hemacs.utils/dom-unpaint-markers))
  (assoc db :with-markers? false))

;; Model ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def desktop-mode
  {:id :desktop
   :name "Desktop Mode"})

(def insert-mode
  {:id :insert
   :name "Insert Mode"})

(def initial-db
  {:enabled? true
   :prev-mode nil
   :mode desktop-mode

   ;; TODO: I think most of these are not needed \/ and maybe some /\
   :which-key nil
   :ctx nil
   :buffer []
   :last-buffer []
   :with-markers? false
   })

(defn enabled?
  [db]
  (:enabled? db))

(defn get-buffer
  ([db]
   (:buffer db))
  ([db key]
   (conj (get-buffer db) key)))

(defn append-buffer
  [db key]
  (assoc db :buffer (get-buffer db key)))

(defn empty-buffer
  [db]
  (assoc db :buffer []))

(defn empty-which-key
  [db]
  (assoc db :which-key nil))

(defn enable-hemacs
  [db]
  (-> db
      (assoc :enabled? true)
      (assoc :buffer [])
      (assoc :last-buffer [])
      (assoc :ctx nil)
      (assoc :which-key nil)
      (assoc :with-markers? false)
      ))

(defn disable-hemacs
  [db]
  (-> db
      (erase-markers)
      (assoc :enabled? false)
      (assoc :buffer [])
      (assoc :last-buffer [])
      (assoc :ctx nil)
      (assoc :which-key nil)
      (assoc :with-markers? false)))

(defn get-ctx
  [db]
  (:ctx db))

(defn set-ctx-app
  [db app-id]
  (assoc-in db [:ctx :app-id] app-id))

(defn get-ctx-session
  [db]
  (:session-id (get-ctx db)))

(defn set-ctx-session
  [db app-id]
  (assoc-in db [:ctx :session-id] app-id))

(defn get-mode
  [db]
  (:mode db))

(defn set-mode
  [db mode]
  (-> db
      (assoc :mode mode)
      (assoc :buffer [])))

(defn get-mode-id
  [db]
  (:id (get-mode db)))

(defn persist-mode
  [db]
  (assoc db :prev-mode (get-mode db)))

(defn get-prev-mode
  [db]
  (:prev-mode db))

(defn set-prev-mode
  [db]
  (set-mode db (get-prev-mode db)))

(defn is-desktop-mode?
  [db]
  (= (get-mode-id db) :desktop))

(defn set-desktop-mode
  [db]
  (set-mode db desktop-mode))

(defn set-insert-mode
  [db]
  (-> db
      (persist-mode)
      (set-mode insert-mode)))

(defn is-insert-mode?
  [db]
  (= (get-mode-id db) :insert))

;; process-input magic ;;

(defn- handle-result-opts
  [db opts]
  (if-not (nil? opts)
    (let [with-markers? (:with-markers? opts)]
      (assoc db :with-markers? with-markers?))
    db))

(defn- handle-result-nomatch
  ([db key]
   (handle-result-nomatch db key "No matching keybindings"))
  ([db key reason]
   (-> db
       (assoc :last-buffer (get-buffer db key))
       (assoc :buffer [])
       (assoc :output [:no-match reason])
       (assoc :which-key nil)
       (erase-markers))))

(defn- handle-result-exact-match
  ([db key]
   (handle-result-exact-match db key ()))
  ([db key actions]
   (doseq [action actions]
     (he/dispatch action))
   (-> db
       (assoc :last-buffer (get-buffer db key))
       (assoc :buffer [])
       (assoc :output [:exact-match])
       (assoc :which-key nil)
       (erase-markers))))

(defn- handle-result-multiple-match
  [db key [remaining opts]]
  (-> db
      (assoc :last-buffer (get-buffer db key))
      (assoc :buffer (get-buffer db key))
      (assoc :output [:multiple-match])
      (assoc :which-key remaining)
      (handle-result-opts opts)))

(defn- handle-result
  [result db key]
  (match result
         [:no-match] (handle-result-nomatch db key)
         [:no-match reason] (handle-result-nomatch db key reason)
         [:exact-match] (handle-result-exact-match db key)
         [:exact-match actions] (handle-result-exact-match db key actions)
         [:multiple-match & args] (handle-result-multiple-match db key args)
         else (he.error/runtime (str "Unknown hemacs result " else))))

(defn- process-input-enabled-escape
  [db esc-key]
  (if (empty? (get-buffer db))
    (-> db
        (set-desktop-mode) ;; TODO: Wrong. Dispatch current mode and ask
        (assoc :which-key nil)
        (assoc :last-buffer [])
        (assoc :output nil))
    (-> db
        (empty-buffer)
        (empty-which-key)
        (erase-markers))))

(defn- process-input-enabled-control
  "Processes the input when hemacs is enabled and the user is disabling it with
  `Control`"
  [db ctrl-key]
  (match (get-buffer db ctrl-key)
         ["Control"] (append-buffer db ctrl-key)
         ["Control" "Control"] (append-buffer db ctrl-key)
         ["Control" "Control" "Control"] (disable-hemacs db)
         _ (-> db
               (empty-buffer)
               (empty-which-key))))

(defn- process-input-enabled-dispatch
  [gdb db key]
  (let [mode-id (if (= "Space" (first (get-buffer db key)))
                  :desktop
                  (get-mode-id db))
        buffer (get-buffer db key)
        ctx (get-ctx db)
        xargs nil]
    (-> mode-id
        (hemacs.dispatcher/dispatch-input gdb buffer ctx xargs)
        (handle-result db key))))

(defn- process-input-enabled-desktop
  [gdb db key]
  (if (or
       (= key "Space")
       (= "Space" (first (get-buffer db))))
    (process-input-enabled-dispatch gdb db key)
    (handle-result-nomatch db key "Try <Space>")))

(defn- process-input-enabled-insert-mode
  [gdb db key]
  (if (= key "Escape")
    (do
      (process-input-enabled-dispatch gdb db key)
      (set-prev-mode db))
    db))

(defn- process-input-enabled
  "Processes the input when hemacs is enabled"
  [gdb db key]
  (cond
    (is-insert-mode? db) (process-input-enabled-insert-mode gdb db key)
    (= key "Control") (process-input-enabled-control db key)
    (= key "Escape") (process-input-enabled-escape db key)
    (or (is-desktop-mode? db)
        (= key "Space")) (process-input-enabled-desktop gdb db key)
    :else (process-input-enabled-dispatch gdb db key)))

(defn- process-input-disabled
  "Processes the input when hemacs is disabled."
  [db key]
  (match (get-buffer db key)
         ["Control"] (append-buffer db key)
         ["Control" "Control"] (append-buffer db key)
         ["Control" "Control" "Control"] (enable-hemacs db)
         _ (empty-buffer db)))

(defn process-input
  [gdb db key]
  (if (enabled? db)
    (process-input-enabled gdb db key)
    (process-input-disabled db key)))

;; find-mode

(defn- find-mode-app
  [db app-type app-id]
  (let [app-mode-info (dispatch-app app-type :mode-info)
        new-mode (if (nil? app-mode-info)
                   desktop-mode
                   app-mode-info)]
    (-> db
        (set-mode new-mode)
        (set-ctx-app app-id))))

(defn- find-mode-noapp
  [db]
  (-> db
      (set-desktop-mode)
      (set-ctx-app nil)))

(defn- find-mode-session
  [db session-id]
  (-> db
      (set-desktop-mode)
      (set-ctx-session session-id)))

(defn find-mode
  [db {app-type :focused-app-type
       app-id :focused-app-id
       session-id :session-id}]
  (if (= session-id (get-ctx-session db))
    (if (nil? app-type)
      (find-mode-noapp db)
      (find-mode-app db app-type app-id))
    (find-mode-session db session-id)))

;; misc

(defn input-focused-in
  [db]
  (-> db
      (set-insert-mode)
      (empty-buffer)
      (empty-which-key)))

(defn input-focused-out
  [db]
  (-> db
      (set-prev-mode)))

;; Bootstrap

(defn bootstrap []
  initial-db)
