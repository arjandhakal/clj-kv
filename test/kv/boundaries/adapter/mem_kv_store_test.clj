(ns kv.boundaries.adapter.mem-kv-store-test
  (:require [clojure.test :refer :all]
            [kv.boundaries.adapter.mem-kv-store :as sut]
            [kv.boundaries.port.kv-store :as port]
            [integrant.core :as ig]))

(deftest test-mem-kv-store-unit
  (let [memtable (atom {})
        threshold 10
        manifest-path "/tmp/clj_kv_test_unit"
        store (sut/->MemKVStore memtable threshold manifest-path)]

    (testing "Store and Fetch logic"
      (port/store! store "foo" "bar")
      (is (= "bar" (port/fetch store "foo"))))

    (testing "Reset logic"
      (port/reset! store)
      (is (nil? (port/fetch store "foo"))))))
