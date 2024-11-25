(ns technical-assessment.middleware
  (:require [taoensso.timbre :as logger]
            [technical-assessment.render-html :as html]
            [technical-assessment.ux.pages.general :as general-pages]))


(defn wrap-exceptions [handler]
  (fn [request]
    (try

      (handler request)

      (catch Exception exception
        (logger/error "Unexpected HTTP server error" {} exception)

        {:status 500
         :body (html/render
                (general-pages/unexpected-error-page))}))))
