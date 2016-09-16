(ns boskatt16.api
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [cheshire.core :as json]
			[boskatt16.rsv :refer :all]
            ))

(defn get-base-uri [request]
  "Generate a base uri from a ring request. For example 'http://localhost:5000/api'."
  (let [scheme (name (:scheme request))
        context (:context request)
        hostname (get (:headers request) "host")]
    (str scheme "://" hostname context)))

(defn json-response [data & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/hal+json; charset=utf-8"}
   :body    (json/generate-string data)})

(defroutes api-routes
           (context "/api" []
             (GET "/" request
               (let [base-uri (get-base-uri request)
                     hal-links {:_links {:self {:href base-uri} :greet {:href (str base-uri "/greet{?name}") :templated true}}}]
                 (json-response hal-links)))
             (GET "/greet" [name :as request]
               (let [base-uri (get-base-uri request)]
                 (json-response {:greeting (str "Greetings " name) :_links {:self {:href (str base-uri "/greet?name=" name)}}})))
             (GET "/skatt" request
               (json-response (skatt-body)))
             (GET "/skatt1" request
               (json-response {:body [{:SE-AB "32.3"} {:SE-C "34.4"} {:SE-E "32.3"} {:SE-F "33.9"} {:SE-G "31.0"}]}))
             (ANY "*" []
               (route/not-found "404.html"))))

(def rest-api
  (handler/api api-routes))

