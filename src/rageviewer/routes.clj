(ns rageviewer.routes
  (:use compojure.core)
  (:use [rageviewer actions services helper]
        [ring.adapter.jetty :only [run-jetty]])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [clojure.tools.logging :as logging]))


(defn app-init []
  (do
    (init-db)
    (logging/info "database initialized")
    (refresh-rages)))

(defroutes default-routes
  (GET "/" [] (redirect-to "index.html"))
  (GET "/rage/:id" [] get-rage)
  (GET "/rages/:channel" [] get-rages)
  (POST "/viewed" [] viewed)
  (GET "/top" [] get-top)
  (POST "/login" [] login)
  (GET "/logout" [] logout)
  (GET "/login-status" [] login-status)
  (POST "/upvote" [] upvote)
  (POST "/downvote" [] downvote)
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (handler/site default-routes))


(defn -main []
  (app-init)
  (let [port (Integer/parseInt (get (System/getenv) "VCAP_APP_PORT" "8080"))]
    (run-jetty app {:port port})))