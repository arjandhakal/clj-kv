(ns dev
  (:require [api :as a]
            [memtable.core :as mc]))





(comment
  ;; Starting the server
  (a/start-server {})

  ;; Stopping the server
  (a/stop-server)

  mc/mem-table

  :-)
