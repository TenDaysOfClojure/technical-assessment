(ns technical-assessment.core
  (:require [technical-assessment.config :as config]
            [technical-assessment.http-server.server :as http-server]))


;; Main entry point for HTTP server when run using:
;;
;; clj -M -m technical-assessment.core {}
;;
;; See README for using different ports
(defn -main [& port]

  (config/setup-config!)

  (let [port (or (first port) config/default-http-port)]
    (http-server/start-server! :port port)))
