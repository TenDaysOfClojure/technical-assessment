(ns scratch
  (:require [technical-assessment.http-server.server :as http-server]
            [technical-assessment.config :as config]
            [technical-assessment.database.core :as database]))

;; This file contains common scratch code that is used during development

(comment

  ;; Setup the web app's config
  (config/setup-config!)

  ;; HTTP server management

  (http-server/start-server! :port config/default-http-port)

  (http-server/stop-server! config/default-http-port)

  (http-server/stop-all-servers!)


  ;; Database

  (database/save-entity (config/current-database) :user
                        {:entity/id "AB" :user/name "Alice" :user/age 30})

  (database/find-entity-by-id (config/current-database) :user "AB")

  (database/find-entity (config/current-database) :user {:user/name "Alice"})

  (database/find-all-entities (config/current-database) :user)

  )
