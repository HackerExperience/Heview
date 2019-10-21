(ns web.hud.conky.widgets.campaign.db)

(defn format-mission-info
  [mission]
  (let [[name step] (.split (:code mission) "@")
        progress (str (:progress mission) "%")]
    {:name name
     :step step
     :progress progress}))
