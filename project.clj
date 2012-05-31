(defproject loci "0.1.0-SNAPSHOT"
  :description "ClojureScript internationalization library."
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/google-closure-library "0.0-1376"]]
  :plugins [[lein-cljsbuild "0.2.1"]]
  :cljsbuild {:builds [{:source-path "src"
                        :compiler {:output-to "out/loci.js"
                                   :output-dir "out"
                                   :optimizations :whitespace
                                   :pretty-print true}}]})
