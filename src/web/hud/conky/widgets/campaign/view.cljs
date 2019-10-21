(ns web.hud.conky.widgets.campaign.view
  (:require [he.core :as he]
            [web.hud.conky.ui :as ckui]
            [game.story.lang :as story-l]))

(defn render-mission-info
  [contact-id]
  (let [mission
        (he/subscribe [:web|hud|conky|widget|campaign|mission-info contact-id])
        lang-mission-name (story-l/_ "mission_name" contact-id (:name mission))
        lang-step-name (story-l/_ "step_name" contact-id (:step mission))]
    [:<>
     [:div.hud-ckui-row
      [:span.hud-ckui-label.hud-ckui-fill-1 "Mission: "]
      [:span.hud-ckui-item lang-mission-name]]
     [:div.hud-ckui-row
      [:span.hud-ckui-label.hud-ckui-fill-1 "Step: "]
      [:span.hud-ckui-item lang-step-name]]
     [:div.hud-ckui-row
      [:span.hud-ckui-label "Progress: "]
      [ckui/progress-bar (:progress mission) ""]]]))

(defn view []
  [:<>
   [render-mission-info :friend]
   [:div.hud-ckui-row
    [:div.hud-ckui-button.hud-ckw-cpg-button-chat "Chat"]
    [:div.hud-ckui-fill-1]
    [:div.hud-ckui-button "Help"]
    [:div.hud-ckui-button "Restart"]]])
