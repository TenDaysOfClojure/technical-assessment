(ns scratch
  (:require [technical-assessment.http-server.server :as http-server]
            [technical-assessment.config :as config]
            [technical-assessment.database.core :as database]
            [technical-assessment.integration.cloudinary :as integration.cloudinary]))

;; This file contains common scratch code that is used during development

(comment

  ;; Setup the web app's config
  ;; This MUST be run before any other code that uses the config
  ;; it's called in the main entry point of the app here `technical-assessment.core/-main`
  (config/setup-config!)

  ;; HTTP server management

  (http-server/start-server! :port config/default-http-port)

  (http-server/stop-server! config/default-http-port)

  (http-server/stop-all-servers!)

  ;; Database scratch

  (database/save-entity (config/current-database) :users
                        {:entity/id "AB" :user/name "Alice" :user/age 30})

  (database/find-entity-by-id (config/current-database) :users "e1cae4a7-7cb2-4f2b-a16b-8f6c4ee254de")

  (database/find-all-entities (config/current-database) :user)

  (database/delete-entity-by-id (config/current-database) :users "e1cae4a7-7cb2-4f2b-a16b-8f6c4ee254de")

  ;; Cloudinary

  ;; Without specifying a public-id
  (integration.cloudinary/upload-image-using-image-url
   config/default-cloudinary-config
   "https://picsum.photos/800/600")


  ;; Specifying a public-id & tags
  (integration.cloudinary/upload-image-using-image-url
   config/default-cloudinary-config
   "https://picsum.photos/800/600"
   :public-id "my-test-imagex"
   :tags ["user profile picture"])

  )
