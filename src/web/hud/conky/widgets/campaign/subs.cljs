(ns web.hud.conky.widgets.campaign.subs
  (:require [re-frame.core :as rf]
            [he.core :as he]
            [web.hud.conky.widgets.campaign.db :as campaign.db]))

(rf/reg-sub
 :web|hud|conky|widget|campaign|mission-info
 (fn [[_ contact-id]]
   [(he/subscribed [:game|story|contact|mission contact-id])])
 (fn [[mission]]
   (campaign.db/format-mission-info mission)))
