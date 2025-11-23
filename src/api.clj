(ns api
  (:require [ring.adapter.jetty :as jetty]
            [memtable.core :as mc ]))

(def server (atom nil))

(defn handler [req]
  (let [req-method (:request-method req)
        key        (subs (:uri req) 1)
        body-stream (:body req)]
    (cond (= req-method :put) (do
                                (mc/store {:key key :value (slurp body-stream)})
                                {:status 200})
          (= req-method :get) (if-let [value (mc/fetch key)]
                                {:status 200
                                 :body value}
                                {:status 404}))))

(defn start-server [opts]
  (when-not @server
    (reset! server (jetty/run-jetty #'handler {:port (or (:port opts) 3003)
                                               :join? false}))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

