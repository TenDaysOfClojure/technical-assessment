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


  (GET urls/auth-facebook-call-back-route {{:keys[code state]} :params}
       (let [;; Note we use Facebook login API `state` param to pass the action to take
             ;; `state` is the name of the parameter facebook uses to allow including extra data
             ;; for authentication callbacks. `action-to-take` here can be `sign-up` or `login`.
             action-to-take      state

             user-data           (integration.facebook-auth/fetch-facebook-user-details code)

             profile-pic-url     (get-in user-data [:picture :data :url])

             {:keys [public-id]} (integration.cloudinary/upload-image-using-image-url
                                  config/default-cloudinary-config
                                  profile-pic-url)

             user-details        {:user-id (str (java.util.UUID/randomUUID))
                                  :profile-pic.integration.cloudinary/public-id public-id
                                  :profile-pic/url (integration.cloudinary/public-image-url public-id)}

             ;; TODO mock saving user to database
             ]

         (str action-to-take " user: "
              user-data
              "  "

              user-details

              )))


  ;; General 404 / page not found handler,
  ;; Note the 500 / unexpected error handler is implemented in middleware.
  (route/not-found (html/render
                    (general-pages/not-found-page))))


(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      (wrap-exceptions)))
