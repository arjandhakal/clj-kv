(ns utils.kv-data-gen
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]))


(defn kv-test-data []
  (let [put-file (io/resource "kv-data.edn")]
    (-> put-file
        (slurp)
        (edn/read-string))))


(spit "test/resources/kv-data.edn" {})

