(ns web.boot.view)

(defn view []
  [:div.boot
   [:div.boot-row1
    [:div.boot-row1-text
     [:span "F2 - Setup Utility"]
     [:span "Esc - Boot menu"]]]
   [:div.boot-row2
    [:div.boot-row2-splash]]
   [:div.boot-row3
    [:span "Bios Revision 2.01.9"]]])
