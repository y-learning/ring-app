(ns ring-app.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.http-response :as response]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [compojure.core :refer [GET POST defroutes context]]))

(defn response-handler [request]
  (response/ok
    (str "<html><body> Your IP address is: "
         (:remote-addr request)
         "</body></html>")))

(defroutes handler
           (GET "/" request response-handler)
           (GET "/:id" [id] (str "<p>The id is: " id "</p>"))
           (POST "/json" [id] (response/ok {:result id}))
           (GET "/foo/foo" request (clojure.string/join "," (keys request))))

(defn wrap-nocache [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))

(defn wrap-formats [handler]
  (wrap-restful-format handler
                       {:formats [:json-kw :transit-json :transit-msgpack]}))

(defn display-profile [id]
  (str "<p>The display-profile is: " id "</p>"))

(defn display-settings [id]
  (str "<p>The display-settings is: " id "</p>"))

(defn change-password [id]
  (str "<p>The change-password is: " id "</p>"))

(defroutes user-routes
           (context "/user/:id" [id]
             (GET "/profile" [] (display-profile id))
             (GET "/settings" [] (display-settings id))
             (GET "/change-password" [] (change-password id))))

(defn -main []
  (println "The server is starting...")
  (jetty/run-jetty (-> #'handler wrap-nocache wrap-reload wrap-formats)
                   {:port 3000 :join? false}))