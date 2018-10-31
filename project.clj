(defproject hetry "0.1.0-SNAPSHOT"
  :description ""

  :url ""

  :dependencies [[org.clojure/core.match "0.3.0-alpha5"]
                 [com.bhauman/figwheel-main "0.1.9"]
                 [com.bhauman/rebel-readline-cljs "0.1.4"]
                 ;; [reagent "0.7.0"]
                 [re-frame "0.10.5"]
                 [cljs-ajax "0.7.4"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [day8.re-frame/async-flow-fx "0.0.11"]]

  :resource-paths ["resources" "target"]
  
  :clean-targets ^{:protect false} ["target/public"]
  
  :profiles {:dev {:dependencies [[com.bhauman/figwheel-main "0.1.5"]
                                  [com.bhauman/rebel-readline-cljs "0.1.4"]]}}

  :aliases {"fig"       ["trampoline" "run" "-m" "figwheel.main"]
            "fig:build" ["trampoline" "run" "-m" "figwheel.main" "-b" "dev" "-r"]})

