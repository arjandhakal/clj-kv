(ns api-test
  (:require [clojure.test :refer :all]
            [api :as SUT]
            [clj-http.client :as client]
            [memtable.core :as mc]
            [utils.kv-data-gen :as u-kdg]))

(def port 3009)
(def url (str "http://localhost:" port))

(deftest test-kv
  (let [_       (SUT/start-server {:port port})]
    (client/put (str url "/foo") {:body "bar"})
    (client/put (str url "/x") {:body "y"})


    (is (= "bar" (:body (client/get (str url "/foo")))))
    (is (= "y" (:body (client/get (str url "/x")))))

    ;; Stopping Server, Cleaning Atom
    (SUT/stop-server)
    (mc/clear-memtable)))

(deftest multiple-test-kv
  (is (= {} (u-kdg/kv-test-data))))


(comment

  (client/get (str url "/zd"))
  
  :-)
