(ns korn.handler
  (:require [compojure.core :refer [defroutes]]            
            [compojure.route :as route]
            [compojure.handler :as handler]
            [noir.util.middleware :as middleware]
            [korn.routes :refer [home-routes]]
            [korn.models.schema :as schema]
            [shoreleave.middleware.rpc :refer [wrap-rpc defremote remote-ns]]))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn destroy []
  "Destroyd")

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (if-not (schema/actualized?) (schema/actualize)))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  "Shutting down")


(remote-ns 'korn.api :as "api")

(def app (middleware/app-handler
           ;;add your application routes here
           [home-routes app-routes]
           ;;add custom middleware here           
           :middleware [wrap-rpc handler/site]
           ;;add access rules here
           ;;each rule should be a vector
           :access-rules []))


(def war-handler (middleware/war-handler app))

