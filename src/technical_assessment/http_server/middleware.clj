(ns technical-assessment.http-server.middleware
  (:require [technical-assessment.http-server.render-html :as html]
            [technical-assessment.ux.pages.general :as general-pages]
            [taoensso.timbre :as timbre-logger]
            [ring.logger.protocols :refer [Logger]]))


(defn wrap-exceptions [handler]
  (fn [request]
    (try

      (handler request)

      (catch Exception exception
        (timbre-logger/error "Unexpected HTTP server error occurred during request"
                             {:uri (:uri request)}
                             exception)

        {:status 500
         :body (html/render
                (general-pages/unexpected-error-page))}))))


(def logger-keys-to-redact #{"x-api-key"
                             "api-key"
                             "password"
                             "email-address"
                             "username"
                             "Authorization"
                             "authorization-token"
                             "user-authorisation-token"
                             "code"})

;; Defines a custom request logger that creates spacious logging by putting a newline
;; between logging outputs to make it slighlty more readable
(defrecord WebRequestLogger []
  Logger

  (add-extra-middleware [_ handler] handler)

  (log [_ level throwable message]
    (timbre-logger/log level throwable message)

    #_(.write *out* "\n")
    (flush)))
