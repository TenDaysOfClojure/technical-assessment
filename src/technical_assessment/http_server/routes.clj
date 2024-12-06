(ns technical-assessment.http-server.routes
  (:require
   ;; HTTP / Web server
   [compojure.core :refer [defroutes GET]]
   [compojure.route :as route]
   [ring.util.response :as response]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [technical-assessment.http-server.middleware :as app-middleware]
   [ring.logger :as request-logger]
   [taoensso.timbre :as logger]

   ;; Intergation
   [technical-assessment.integration.facebook.auth :as integration.facebook-auth]
   [technical-assessment.database.core :as database]

   ;; General config
   [technical-assessment.config :as config]
   [technical-assessment.urls :as urls]

   ;; Sever side rendering, layouts and content etc
   [technical-assessment.http-server.render-html :as html]
   [technical-assessment.ux.pages.home :as home-page]
   [technical-assessment.ux.pages.general :as general-pages]
   [technical-assessment.ux.pages.user :as user-pages]
   [technical-assessment.ux.pages.demo :as demo-pages]
   [technical-assessment.domain.user :as domain.user]))


(defroutes app-routes

  (GET urls/home-route []
       (html/render (home-page/page)))

  ;; This end-point simulates a unexpected exception
  (GET "/sample-error" []
       (throw (Exception. "This is a sample / test error")))

  (GET "/ux/demo" []
        (html/render (demo-pages/story-book)))

  ;; Handles sign-up and login actions via a URL param named :action
  (GET (str urls/auth-facebook-base-route "/:action")  [action]
       (let [auth-url (integration.facebook-auth/authenticaiton-redirect-url
                       config/facebook-auth-config
                       action)]
         (response/redirect auth-url)))


  (GET urls/auth-facebook-call-back-route {{:keys[code state]} :params}
       ;; Note we use Facebook login API `state` param determine if it's a login or sign-up action.
       ;; The `state` parameter is what facebook authentication uses to allow including extra data
       ;; for authentication callbacks. In this case the `state` will be either `sign-up` or `login`.

       (logger/info "Facebook auth callback received for" state)

       (let [user-details (domain.user/login-or-sign-up-user-via-facebook code)

             user-id      (:entity/id user-details)]

         (response/redirect (urls/user-dashboard-route user-id))))


  (GET (urls/user-dashboard-route ":user-id") [user-id]
       (let [user-details (database/find-entity-by-id
                           (config/current-database)
                           :users user-id)]

         (html/render (user-pages/dashboard-page user-details))))


  ;; General 404 / page not found handler,
  ;; Note the 500 / unexpected error handler is implemented as middleware
  ;; in `technical-assessment.http-server.middleware/wrap-exceptions`.
  (route/not-found (html/render
                    (general-pages/not-found-page))))


;; This represents the main web app routes and middleware and is used
;; in conjunction with the Jetty server in `technical-assessment.http-server.server`.
(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      (request-logger/wrap-with-logger {:timing false
                                        :logger (app-middleware/->WebRequestLogger)
                                        :redact-keys app-middleware/logger-keys-to-redact})
      (app-middleware/wrap-exceptions)))
