(ns dev
  (:require [kv.core :as core]
            [integrant.core :as ig]
            [integrant.repl :as ig-repl]
            [integrant.repl.state :as ig-state]
            [clojure.tools.namespace.repl :as repl]
            [clojure.walk :as walk]
            [kaocha.repl :as main]
            [portal.api :as p]))



;; Portal Setup
(defonce portal (p/open))

(add-tap #'p/submit)

(defn inspect [data] (tap> data))

;; Integrant Setup
(ig-repl/set-prep! (fn []
                     (let [config (core/load-config :dev)]
                       (ig/load-namespaces config)
                       config)))

;; Development Helpers

(defn start! [] (ig-repl/go))
(defn stop! [] (ig-repl/halt))
(defn restart! [] (ig-repl/reset))
(defn system [] ig-state/system)
(defn refresh [] (repl/refresh))


;; Code reloading with system restart
(defn refresh-and-restart! []
  (when-let [result (repl/refresh :after 'dev/start!)]
    (println "Refreshed:" result)))

(def reset refresh-and-restart!)

(defn test []
  (ig-repl/reset)
  (main/run :unit))
 
(comment
  (start!)                   ;; Start system
  (inspect (system))         ;; View system in portal
  (inspect @(get-in (system) [:kv.boundaries.adapter.mem-kv-store/store :memtable]))
  (refresh-and-restart!)     ;; Reload code + restart system
  (refresh)             ;; Just reload code
  (stop!)                   ;; Stop system
)


(comment
  ;; Trying out libraries
  ;; Trying out repl 
  (require '[clojure.repl.deps :refer [add-libs]])

  (add-libs '{org.clojure/data.json {:mvn/version "2.5.1"}})
  (add-libs '{org.clojure/core.async {:mvn/version "1.8.741"}})

  (add-libs '{djblue/portal {:mvn/version "0.62.2"}})
  (add-libs '{integrant/repl {:mvn/version "0.5.0"}})

  (require '[clojure.data.json :as json])


  (->
   (json/write-str {:a 1 :b 2 :c {:d "xxx"}})
   (json/read  :key-fn keyword))

  :-)



