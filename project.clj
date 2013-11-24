(defproject korn "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src"]
  :plugins [[lein-cljsbuild "1.0.0"]
            [lein-ring "0.8.8"]]
  :cljsbuild {:crossovers [korn.validations]
              :builds [{
                        :source-paths ["src"]
                        :compiler {
                                   :output-to "resources/public/js/korn.js"
                                   :optimization :whitespace}}]}
  :hooks [leiningen.cljsbuild]
  :ring {
         :handler korn.handler/war-handler
         :init korn.handler/init
         :destroy korn.handler/destroy}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2030"]
                 [ring "1.2.0"]
                 [compojure "1.1.5"]
                 [korma "0.3.0-RC5"]
                 [lobos "1.0.0-beta1"]
                 [postgresql "9.1-901.jdbc4"]
                 [hiccup "1.0.4"]
                 [lib-noir "0.6.6"]
                 [enfocus "2.1.1-SNAPSHOT"] ; DOM manipulation
                 [domina "1.0.2-SNAPSHOT"] ; DOM manipulation
                 [clj-yaml "0.4.0"]
                 [shoreleave/shoreleave-remote "0.3.0"] ; ajax stuff
                 [shoreleave/shoreleave-remote-ring "0.3.0"] ;ajax stuff
                 [shoreleave/shoreleave-browser "0.3.0"] ; history stuff
                 [jkkramer/verily "0.6.0"]
                 [com.cemerick/valip "0.3.2"]                                   ; validations
                 [org.clojure/google-closure-library-third-party "0.0-2029-2"]    ; needed for validations
                 ])
