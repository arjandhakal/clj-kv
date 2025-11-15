(ns api
  (:require [ring.adapter.jetty :as jetty]))

(defonce kv-memory (atom {}))
(def server (atom nil))

(defn handler [req]
  (let [req-method (:request-method req)
        key        (subs (:uri req) 1)
        body-stream (:body req)]
    (cond (= req-method :put) (do
                                (swap! kv-memory assoc key (slurp body-stream))
                                {:status 200})
          (= req-method :get) (if-let [value (get @kv-memory key)]
                                {:status 200
                                 :body value}
                                {:status 404}))))

(defn start-server []
  (when-not @server
    (reset! server (jetty/run-jetty #'handler {:port 3003
                                        :join? false}))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

