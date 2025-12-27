(ns utils.file
  (:require [clojure.java.io :as io]))

(defn directory-exists? [path]
  (let [f (io/file path)]
    (and (.exists f) (.isDirectory f))))

(defn file-exists? [path]
  (let [f (io/file path)]
    (and (.exists f) (.isFile f)))) ;; Fixed semantic issue

(defn save-file! 
  "Saves content to file-path, creating parent directories if needed.
   Performs an atomic write (write-temp-then-move) to prevent corruption.
   
   Note: Does not support :append true due to atomicity strategy."
  [file-path content]
  (io/make-parents file-path) ;; Use io/ directly
  (let [file     (io/file file-path)
        tmp-file (io/file (str file-path ".tmp"))]
    (spit tmp-file content)
    (.renameTo tmp-file file)))
