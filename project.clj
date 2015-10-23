(defproject cljs-browser-repl "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.145"]
                 [cljsjs/jqconsole "2.12.0-0"]
                 [reagent "0.5.1"]]

  :plugins [[lein-cljsbuild "1.1.0"]
            [lein-doo "0.1.6-SNAPSHOT"]
            [lein-figwheel "0.4.1" :exclusions [cider/cider-nrepl]]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target" ]
  :hooks [leiningen.cljsbuild]
  :source-paths ["src/clj"]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs" "test/cljs"]
                        :figwheel {:on-jsload "launcher.test/run"
                                   :css-dirs ["resources/public/styles"]}
                        :compiler {:main cljs-browser-repl.core
                                   :output-to "resources/public/js/compiled/cljs-browser-repl.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :optimizations :none
                                   :source-map-timestamp true}}
                       {:id "test"
                        :source-paths ["src/cljs" "test/cljs"]
                        :compiler {:main launcher.runner
                                   :output-to "resources/private/test/compiled/cljs-browser-repl.js"
                                   :pretty-print false}}
                       {:id "min"
                        :source-paths ["src/cljs"]
                        :compiler { ;; :main cljs-browser-repl.core ;; https://github.com/emezeske/lein-cljsbuild/issues/420
                                   :output-to "resources/public/js/compiled/cljs-browser-repl.js"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :externs ["resources/cljs-browser-repl.ext.js"]}}]}

  :aliases {"fig-dev" ^{:doc "Start figwheel with dev profile."} ["figwheel" "dev"]
            "fig-dev*" ^{:doc "Clean and start figwheel with dev profile"} ["do" "clean" ["figwheel" "dev"]]
            "minify" ^{:doc "Clean and compile sources minified for production."} ["do" "clean" ["cljsbuild" "once" "min"]]
            "deploy" ^{:doc "Clean, compile (minified) sources, test and then deploy."} ["do" "clean" ["test" ":integration"] ["deploy" "clojars"]]
            "test-phantom" ^{:doc "Execute unit tests with PhantomJS (must be installed)."} ["doo" "phantom" "test" "once"]
            "test-phantom*" ^{:doc "Clean and execute unit tests with PhantomJS (must be installed)."} ["do" "clean" ["doo" "phantom" "test" "once"]]
            "test-slimer" ^{:doc "Execute unit tests with SlimerJS (must be installed)."} ["doo" "slimer" "test" "once"]
            "test-slimer*" ^{:doc "Clean and execute unit tests with SlimerJS (must be installed)."} ["do" "clean" ["doo" "slimer" "test" "once"]]}

  :profiles {:dev {:dependencies [[com.cemerick/piggieback "0.1.5"]
                                  [org.clojure/tools.nrepl "0.2.11"]]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :figwheel {:nrepl-port 5088
                              ;; Load CIDER, refactor-nrepl and piggieback middleware
                              :nrepl-middleware ["cider.nrepl/cider-middleware"
                                                 "refactor-nrepl.middleware/wrap-refactor"
                                                 "cemerick.piggieback/wrap-cljs-repl"]}}})