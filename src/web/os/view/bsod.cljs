(ns web.os.view.bsod)

(defn fun [el attr]
  (when-not (nil? el)
    (set! (.-visibility (.-style el)) attr)))
(defn interval-fn-show [el-blink] (fun el-blink "visible"))
(defn interval-fn-hide [el-blink] (fun el-blink "hidden"))

(defn brilha-brilha-estelinha []
  (let [el-blink (.querySelector js/document ".os-bsod-blink")]
    (js/setTimeout #(interval-fn-show el-blink) 1)
    (js/setTimeout #(interval-fn-hide el-blink) 1000)))

;; TODO: Create/remove interval on component mount/unmount
(defn view []
  (js/setInterval #(brilha-brilha-estelinha) 2000)
  [:div#os-bsod
   [:div.os-bsod-container
    [:span.os-bsod-title "Windows"]
    [:p.top (str "A problem has been detected and Windows has been shut down "
                 "to prevent damage to your computer.")]
    [:br]
    [:p "PAGE_FAULT_IN_NONPAGED_AREA"]
    [:br]
    [:p (str "If this is the first time you've seen this Stop error screen, "
             "restart your computer. If this screen appears again, follow "
             "these steps:")]
    [:br]
    [:p (str "Check to be sure you have adequate disk space. If a driver "
             "is identified in the Stop message, disable the driver or check "
             "with the manufacturer for driver updates. Try changing "
             "video adapters.")]
    [:br]
    [:p (str "Check with your hardware vendor for any BIOS updates. "
             "Disable BIOS memory options such as caching or shadowing. "
             "If you need to use Safe Mode to remove or disable components, "
             "restart your computer, press F8 to select Advanced Startup "
             "options, and then select Safe Mode.")]
    [:br]
    [:p "Technical information:"]
    [:br]
    [:p (str "*** STOP: 0xDEADBEEF (0x80000003, 0x805c49b8, "
             "0xf7a172b4, 0xf7a16fb0)")]
    [:br]
    [:br]
    [:span "Press any key to continue "]
    [:span.os-bsod-blink "_"]]])
