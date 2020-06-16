(ns ring-app.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]))

(defn handler [request]
  (response/response
    (str "<html><body> Your IP address is: "
         (:remote-addr request)
         "</body></html>")))

(defn -main []
  (println "The server is starting...")
  (jetty/run-jetty handler {:port 3000 :join? false}))