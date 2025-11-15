(ns api-test
  (:require [clojure.test :refer :all]
            [api :as SUT]
            [clj-http.client :as client]))

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
    (reset! SUT/kv-memory nil)))


(comment
  SUT/kv-memory
  SUT/server
  (client/get (str url "/zd"))
   ;;
  )
