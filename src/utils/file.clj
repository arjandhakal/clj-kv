(ns utils.file
  (:require [clojure.java.io :as io]))


(defn save-file 
  "Saves file, and makes parent directories 
  if it does not exist."
  [file-path content]

  (io/make-parents file-path)
  (spit file-path content))
