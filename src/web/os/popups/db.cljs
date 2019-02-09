(ns web.os.popups.db
  (:require [web.os.popups.confirm.db]
            [web.os.popups.os-error.db]))

(defn ^:export popup-may-open
  [_ctx popup-type parent-id args xargs]
  [:open-popup :os popup-type parent-id args xargs])
(defn ^:export popup-may-close
  [_ctx popup-type family-ids _state args xargs]
  [:close-popup :os popup-type family-ids args xargs])
(defn ^:export popup-may-focus
  [_ctx popup-type family-ids _state args xargs]
  [:focus-popup :os popup-type family-ids args xargs])
