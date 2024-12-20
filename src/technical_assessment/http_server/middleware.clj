(ns technical-assessment.http-server.middleware
  (:require [technical-assessment.http-server.render-html :as html]
            [technical-assessment.ux.pages.general :as general-pages]
            [taoensso.telemere :as logger]
            [technical-assessment.logging :as logging]
            [clojure.string :as string]))


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


(defn request-logger [handler]
  (fn [{:keys [request-method uri] :as request} ]
    (let [{:keys [status] :as response} (handler request)]
      (logging/info (logging/green-text "Web request")
                    (string/upper-case (name request-method))
                    uri
                    (logging/cyan-text "status" status))
      response)))
