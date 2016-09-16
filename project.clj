(defproject boskatt16 "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.1.8"]
                 [enlive "1.1.6"]
;;				 [hiccup.middleware :refer [wrap-base-url]]
                 [ring/ring-core "1.1.7"]
                 [ring/ring-jetty-adapter "1.1.7"]
				 [clj-http "2.2.0"]
				 [cheshire "5.6.3"]]
  :main boskatt16.main)
