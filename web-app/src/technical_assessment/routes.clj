(ns technical-assessment.routes
  (:require
   ;; HTTP / Web server
   [compojure.core :refer [defroutes GET]]
   [compojure.route :as route]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [technical-assessment.middleware :refer [wrap-exceptions]]

   ;; 3rd party intergation low level tools
   [clj-http.client :as client]
   [cheshire.core :as json]
   [technical-assessment.integration.cloudinary :as integration.cloudinary]
   [technical-assessment.integration.facebook-auth :as integration.facebook-auth]
   [technical-assessment.database.core :as database]

   ;; General config
   [technical-assessment.config :as config]
   [technical-assessment.urls :as urls]

   ;; Sever side rendering, layouts and content etc
   [technical-assessment.render-html :as html]
   [technical-assessment.ux.pages.home :as home-page]
   [technical-assessment.ux.pages.general :as general-pages]))


(defroutes app-routes

  (GET urls/home-route []
       (html/render (home-page/page)))

  ;; This end-point simulates a unexpected exception
  (GET "/sample-error" []
       (throw (Exception. "This is a sample / test error")))


  ;; Handles sign-up and login actions via a URL param named :action
  (GET (str urls/auth-facebook-base-route "/:action")  [action]
       (let [auth-url (str "https://www.facebook.com/v10.0/dialog/oauth?client_id=" (:app-id config/facebook-auth-config)
                           "&redirect_uri=" (:redirect-uri config/facebook-auth-config) "&scope=email"
                           ;; Note we use Facebook login API `state` param to pass the action to take
                           ;; The `response_type` querystring must be `code` for this to work.
                           "&response_type=code&state=" action)]
         ;; Redirects the browser to the facebook auth url
         {:status 302 :headers {"Location" auth-url}}))


  (GET urls/auth-facebook-call-back-route {{:keys[code state]} :params :as params}
       (let [;; Note we use Facebook login API `state` param to pass the action to take
             ;; `state` is the name of the parameter facebook uses to allow including extra data
             ;; for authentication callbacks. `action-to-take` here can be `sign-up` or `login`.
             action-to-take      state

             facebook-user       (integration.facebook-auth/find-facebook-user
                                  config/facebook-auth-config
                                  code)

             profile-pic-url     (get-in facebook-user [:picture :data :url])

             facebook-user-id    (:id facebook-user)

             {:keys [public-id]} (integration.cloudinary/upload-image-using-image-url
                                  config/default-cloudinary-config
                                  profile-pic-url)

             user-details        {:entity/id (str (java.util.UUID/randomUUID))
                                  :user.auth.facebook/user-id facebook-user-id

                                  :user/first-name (:first-name facebook-user)
                                  :user/last-name (:last-name facebook-user)
                                  :user/email-address (:email facebook-user)

                                  :user.profile-pic.integration.cloudinary/public-id public-id
                                  :user.profile-pic/url (integration.cloudinary/public-image-url
                                                         config/default-cloudinary-config
                                                         public-id)}

             saved?             (database/save-entity (config/current-database)
                                                      :users user-details) ]

         (clojure.pprint/pprint user-details)

         (str saved? "" facebook-user " --" user-details)))


  ;; General 404 / page not found handler,
  ;; Note the 500 / unexpected error handler is implemented in middleware.
  (route/not-found (html/render
                    (general-pages/not-found-page))))


(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      (wrap-exceptions)))
