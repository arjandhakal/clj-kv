(ns kv.boundaries.port.kv-store)

(defprotocol KVStore
  (store! [this k v] "Store a key-pair value")
  (fetch [this k] "Retrive value for a key")
  (flush! [this] "Persist in memory data")
  (delete! [this k] "Delete a key")
  (all-keys [this]  "Get all keys")
  (keys-size [this] "Get number of entires")
  (reset! [this] "Reset the store to be empty"))
 

