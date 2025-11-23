(ns dev
  (:require [api :as a]
            [memtable.core :as mc]))





(comment
  ;; Starting the server
  (a/start-server {})

  ;; Stopping the server
  (a/stop-server)

  @mc/memtable

  :-)


(comment
  ;; Trying out libraries
  ;; Trying out repl 
  (require '[clojure.repl.deps :refer [add-libs]])

  (add-libs '{org.clojure/data.json {:mvn/version "2.5.1"}})

  (require '[clojure.data.json :as json])

  (json/write-str [{:a 1 :b 2}])
  (json/write-str @mc/memtable)

  (->
   (json/write-str {:a 1 :b 2 :c {:d "xxx"}})
   (json/read  :key-fn keyword))

  :-)


