(ns boskatt16.main
  (:use ring.adapter.jetty
        compojure.core
        compojure.route)
  (:require [compojure.handler :as handler]
            [net.cgrand.enlive-html :as enlive]
			[boskatt16.api :refer [rest-api]])
  (:gen-class))

(def things ["one" "two" "three" "four"])

(defn extract-body [html]
  (enlive/at html [#{:html :body}] enlive/unwrap))

(enlive/deftemplate layout "layout.html" [title content]
  [#{:title :h1}] (enlive/content title)
  [:div.content] (enlive/substitute (extract-body content)))

(defn show [things]
  (enlive/at (enlive/html-resource "show.html")
             [:li] (enlive/clone-for [thing things] (enlive/content thing))))

(def index (enlive/html-resource "index.html"))

(defroutes app
  (GET "/" [] (layout "Front page" index))
  (GET "/show" [] (layout "Show things" (show things)))
  (not-found "Not Found"))

(defn -main []
  (run-jetty
;;	(-> (routes rest-api app)
;;      (handler/site))
;;      (wrap-base-url))
;;	(handler/site app)
	(handler/site (routes rest-api app))
	{:port 8080}))
