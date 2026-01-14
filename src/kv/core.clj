(ns kv.core
  (:require [clojure.java.io :as io]
            [aero.core :as aero]
            [integrant.core :as ig]))

(defmethod aero/reader 'ig/ref [_ _ value]
  (ig/ref value))

(defn load-config [profile]
  (-> (io/resource "config.edn")
      (aero/read-config {:profile profile})
      (dissoc ::secrets)))

(defn -main []
  (let [config (load-config :dev)]
    (ig/load-namespaces config)
    (ig/init config)))

(comment
  (def system (-main))
  (ig/halt! system))
