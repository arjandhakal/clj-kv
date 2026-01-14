(ns api-test
  (:require [clojure.test :refer :all]
            [kv.core :as core]
            [integrant.core :as ig]
            [clj-http.client :as client]
            [utils.kv-data-gen :as u-kdg]))

(def ^:dynamic *url* nil)

(def system (atom nil))

(defn start-system [f]
  (let [config (core/load-config :test)
        _      (ig/load-namespaces config)
        sys    (ig/init config)
        port   (get-in config [:kv.api/server :port])]
    (reset! system sys)
    (try
      (binding [*url* (str "http://localhost:" port)]
        (f))
      (finally
        (ig/halt! sys)))))

(use-fixtures :once start-system)

(deftest test-kv
  (client/put (str *url* "/foo") {:body "bar"})
  (client/put (str *url* "/x") {:body "y"})

  (is (= "bar" (:body (client/get (str *url* "/foo")))))
  (is (= "y" (:body (client/get (str *url* "/x"))))))

(deftest multiple-test-kv
  (is (= {} (u-kdg/kv-test-data))))
