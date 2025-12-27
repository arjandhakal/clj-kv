(ns memtable.core
  (:require [utils.file :as uf]
            [clojure.string :as str]))

(defonce memtable (atom {}))

(def tmp-location "/tmp/clj_kv/ss_tables")

(defn next-manifest-file []
  (let [manifest-file (str tmp-location  "/manifest.json")
        _  (when-not  (uf/file-exists? manifest-file)
             (uf/save-file! manifest-file ""))
        manifest-data (slurp manifest-file)
        last-file (last (str/split manifest-data #"\n"))]
    (if (seq last-file)
        (str "sst-" (inc (Integer/parseInt (first (subs last-file 4)))))
        "sst-1.json")))

(defn flush-memtable [data]
  (future
    (let [next-manifest (next-manifest-file)]
      ; TODO (Serialize to JSON)
      (uf/save-file! (str tmp-location "/" next-manifest) data)
      ;TODO (Update manifest file)
      )))

(defn fetch [key]
  (get @memtable key))

(defn clear-memtable []
  (reset! memtable {}))

(defn store [{:keys [key value]}]
  (when (>= (count @memtable) 3)
    (flush-memtable @memtable)
    (clear-memtable)) 
  (swap! memtable assoc key value))


