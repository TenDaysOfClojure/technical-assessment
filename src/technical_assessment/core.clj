(ns technical-assessment.core
  (:require [technical-assessment.config :as config]
            [technical-assessment.http-server.server :as http-server]))


;; Main entry point for HTTP server when run using:
;;
;; clj -M -m technical-assessment.core

(defn -main [& port]

  (config/setup-config!)

  (http-server/start-server! :port config/default-http-port))
