(ns kv.boundaries.adapter.mem-kv-store
  (:require [kv.boundaries.port.kv-store :as port]
            [clojure.string :as str]
            [utils.file :as uf]
            [integrant.core :as ig]))

(defn- next-manifest-file [manifest-path]
  (let [manifest-file (str manifest-path  "/manifest.json")
        _  (when-not  (uf/file-exists? manifest-file)
             (uf/save-file! manifest-file ""))
        manifest-data (slurp manifest-file)
        last-file (last (str/split manifest-data #"\n"))]
    (if (seq last-file)
        (str "sst-" (inc (Integer/parseInt (first (subs last-file 4)))))
        "sst-1.json")))

(defn- flush-memtable
  "Flush KV Store"
  [data manifest-path]
  (future
    (let [next-manifest (next-manifest-file manifest-path)]
                                        ; TODO (Serialize to JSON)
      (uf/save-file! (str manifest-path "/" next-manifest) data)
                                        ;TODO (Update manifest file)
      )))

(defrecord MemKVStore [memtable threshold manifest-path]
  port/KVStore
  (fetch [_ k]
    (get @memtable k))
  (store! [this k v]
    (when (> (port/keys-size this) threshold)
      (port/flush! this)
      (port/reset! this))
    (swap! memtable assoc k v))
  (keys-size [_]
    (count @memtable))
  (flush! [_]
    (flush-memtable @memtable manifest-path))
  (reset! [_]
    (reset! memtable {}))
  (delete! [_ k]
    (swap! memtable dissoc k))
  (all-keys [_]
    (keys @memtable))) 

(defmethod ig/init-key ::store [_ {:keys [threshold manifest-path]}]
  (let [memtable (atom {})]
    (->MemKVStore memtable threshold manifest-path)))


(defmethod ig/halt-key! ::store [_ store]
  (let [flush-result (port/flush! store)]
    (when (future? flush-result)
      @flush-result
      (println "Flush complete.."))))
