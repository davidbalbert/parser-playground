(defproject parser-playground "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2268"]
                 [org.clojure/core.async "0.1.303.0-886421-alpha"]
                 [om "0.6.4"]
                 [prismatic/om-tools "0.2.2"]
                 [weasel "0.3.0"]
                 [com.cemerick/piggieback "0.1.3"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :injections [(require 'cemerick.piggieback 'weasel.repl.websocket)
               (defn browser-repl []
                 (cemerick.piggieback/cljs-repl
                  :repl-env (weasel.repl.websocket/repl-env :port 9001)))]

  :source-paths ["src"]

  :cljsbuild {
    :builds [{:id "parser-playground"
              :source-paths ["src"]
              :compiler {
                :output-to "parser_playground.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}]})
