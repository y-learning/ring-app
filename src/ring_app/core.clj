(ns ring-app.core
  (:require [ring.adapter.jetty :as jetty]))

(defn handler [request]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (str "<html> <body> your IP is: "
                 (:remote-addr request)
                 "</body> </html>")})

(defn -main []
  (println "The server is starting...")
  (jetty/run-jetty handler {:port 3000 :join? false}))