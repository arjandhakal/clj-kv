(ns kv.api
  (:require [ring.adapter.jetty :as jetty]
            [kv.boundaries.port.kv-store :as kv-store.port]
            [integrant.core :as ig]))

(defn handler [req mem-kv-store]
  (let [req-method (:request-method req)
        key        (subs (:uri req) 1)
        body-stream (:body req)]
    (cond (= req-method :put) (do
                                (kv-store.port/store! mem-kv-store key (slurp body-stream))
                                {:status 200})
          (= req-method :get) (if-let [value (kv-store.port/fetch mem-kv-store key)]
                                {:status 200
                                 :body  value}
                                {:status 404}))))


(defn app [mem-kv-store]
  (fn [req]
    (handler req mem-kv-store)))

(defmethod ig/init-key ::server [_ {:keys [port mem-kv-store]}]
  (println "Starting kv server on port: " port)
  (let [handler (app mem-kv-store)]
    (jetty/run-jetty handler {:port port
                            :join? false})))

(defmethod ig/halt-key! ::server [_ server]
  (.stop server))
