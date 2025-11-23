(ns memtable.core)

(defonce memtable (atom {}))

(defn flush-memtable []
  (clojure.pprint/pprint "FLUSING..."))

(defn store [{:keys [key value]}]
  (swap! memtable assoc key value)
  ;; do a sync flush for now (later we can move to async)
  (when (>= (count @memtable) 2000)
    (flush-memtable)))

(defn fetch [key]
  (get @memtable key))

(defn clear-memtable []
  (reset! memtable {}))
