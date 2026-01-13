(ns api
  (:require [ring.adapter.jetty :as jetty]
            [memtable.core :as mc ]
            [kv.boundaries.port.kv-store :as kv-store.port]
            [kv.boundaries.adapter.mem-kv-store :as mem-kv-adapter]))

(def server (atom nil))

(defn handler [req]
  (let [req-method (:request-method req)
        key        (subs (:uri req) 1)
        body-stream (:body req)]
    (cond (= req-method :put) (do
                                (kv-store.port/store! mem-kv-adapter/mem-kv-store key (slurp body-stream))
                                {:status 200})
          (= req-method :get) (if-let [value (kv-store.port/fetch mem-kv-adapter/mem-kv-store key)]
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

