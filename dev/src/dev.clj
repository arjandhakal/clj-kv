(ns dev
  (:require [api]
            [memtable.core :as mc]
            [clojure.core.async :as a
             :refer [>!  <! >!! <!!  go chan buffer close! thread alts! alts!! timeout]]))





(comment
  ;; Starting the server
  (api/start-server {})

  ;; Stopping the server
  (api/stop-server)

  @mc/memtable

  :-)


(comment
  ;; Trying out libraries
  ;; Trying out repl 
  (require '[clojure.repl.deps :refer [add-libs]])

  (add-libs '{org.clojure/data.json {:mvn/version "2.5.1"}})
  (add-libs '{org.clojure/core.async {:mvn/version "1.8.741"}})

  (require '[clojure.data.json :as json])

  (json/write-str [{:a 1 :b 2}])
  (json/write-str @mc/memtable)

  (->
   (json/write-str {:a 1 :b 2 :c {:d "xxx"}})
   (json/read  :key-fn keyword))

  :-)




(comment
  (defn hot-dog-machine
    []
    (let [in (chan)
          out (chan)]
      (go (<! in)
          (>! out "hot dog"))
      [in out]))

  (let [[in out] (hot-dog-machine)]
    (>!! in "chi-ching")
    (<!! out))

  (defn hot-dog-machine-v2
    [hot-dog-count]
    (let [in (chan)
          out (chan)]
      (go (loop [hc hot-dog-count]
            (if (> hc 0)
              (let [input (<! in)]
                (if (= 3 input)
                  (do (>! out "hot dog")
                      (recur (dec hc)))
                  (do (>! out "wilted lettuce")
                      (recur hc))))
              (do (close! in)
                  (close! out)))))
      [in out]))

  (let [[in out] (hot-dog-machine-v2 2)]
    (>!! in "pocket lint")
    (println (<!! out))

    (>!! in 3)
    (println (<!! out))

    (>!! in 3)
    (println (<!! out))

    (>!! in 3)
    (println (<!! out)))


  (let [c1 (chan)
        c2 (chan)
        c3 (chan)]
    (go (>! c2 (clojure.string/upper-case (<! c1))))
    (go (>! c3 (clojure.string/reverse (<! c2))))
    (go (println (<! c3)))
    (>!! c1 "redrum"))

  

  

  :-)

