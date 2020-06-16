(ns ring-app.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [ring.middleware.reload :refer [wrap-reload]]))

(defn handler [request]
  (response/response
    (str "<html><body> Your IP address is: "
         (:remote-addr request)
         "</body></html>")))

(defn wrap-nocache [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))

(defn -main []
  (println "The server is starting...")
  (jetty/run-jetty (-> #'handler wrap-nocache wrap-reload)
                   {:port 3000 :join? false}))