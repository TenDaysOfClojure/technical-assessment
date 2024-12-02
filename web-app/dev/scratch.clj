(ns scratch
  (:require [technical-assessment.http-server :as http-server]
            [technical-assessment.config :as config]))

;; This file contains common scratch code that is used during development

(comment

  ;; Setup the web app's config
  (config/setup-config!)

  ;; HTTP server management
  (http-server/start-server! :port config/default-http-port)

  (http-server/stop-server! config/default-http-port)

  (http-server/stop-all-servers!)

  )