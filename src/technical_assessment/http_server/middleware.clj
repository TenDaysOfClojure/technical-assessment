(ns technical-assessment.http-server.middleware
  (:require [technical-assessment.http-server.render-html :as html]
            [technical-assessment.ux.pages.general :as general-pages]
            [taoensso.telemere :as logger]
            [ring.logger.protocols :refer [Logger]]))


(defn wrap-exceptions [handler]
  (fn [request]
    (try

      (handler request)

      (catch Exception exception
        (logger/log! {:level :error
                      :error exception
                      :data {:uri (:uri request)}} 
                     "Unexpected HTTP server error occurred during request")

        {:status 500
         :body (html/render
                (general-pages/unexpected-error-page))}))))


(def logger-keys-to-redact #{:x-api-key
                             :api-key
                             :password
                             :email-address
                             :username
                             :authorization
                             :authorization-token
                             :user-authorisation-token
                             :code})


;; Defines a custom request logger that creates spacious logging by putting a newline
;; between logging outputs to make it slighlty more readable
(defrecord WebRequestLogger []
  Logger

  (add-extra-middleware [_ handler] handler)

  (log [_ level throwable message]
    ;; Only log non-debug messages to reduce noise
    (when (not (= :debug level))
      (logger/log! {:level level :error throwable} message))))
