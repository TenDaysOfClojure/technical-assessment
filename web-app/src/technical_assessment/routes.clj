(ns technical-assessment.routes
  (:require
   ;; HTTP / Web server
   [compojure.core :refer [defroutes GET]]
   [compojure.route :as route]
   [ring.util.response :as response]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [technical-assessment.middleware :refer [wrap-exceptions]]

   ;; Intergatio
   [technical-assessment.integration.cloudinary :as integration.cloudinary]
   [technical-assessment.integration.facebook-auth :as integration.facebook-auth]
   [technical-assessment.database.core :as database]

   ;; General config
   [technical-assessment.config :as config]
   [technical-assessment.urls :as urls]

   ;; Sever side rendering, layouts and content etc
   [technical-assessment.render-html :as html]
   [technical-assessment.ux.pages.home :as home-page]
   [technical-assessment.ux.pages.general :as general-pages]
   [technical-assessment.ux.pages.user :as user-pages]))


(defroutes app-routes

  (GET urls/home-route []
       (html/render (home-page/page)))

  ;; This end-point simulates a unexpected exception
  (GET "/sample-error" []
       (throw (Exception. "This is a sample / test error")))


  ;; Handles sign-up and login actions via a URL param named :action
  (GET (str urls/auth-facebook-base-route "/:action")  [action]
       (let [auth-url (integration.facebook-auth/authenticaiton-redirect-url
                       config/facebook-auth-config
                       action)]
         (response/redirect auth-url)))


  (GET urls/auth-facebook-call-back-route {{:keys[code state]} :params :as params}

       ;; TODO This route now contains in situ soluton to the tech assessment
       ;; the next step would be to refactor this into integration / domain code

       (let [;; Note we use Facebook login API `state` param to pass the action to take
             ;; `state` is the name of the parameter facebook uses to allow including extra data
             ;; for authentication callbacks. `action-to-take` here can be `sign-up` or `login`.
             action-to-take        state

             facebook-user         (integration.facebook-auth/find-facebook-user
                                    config/facebook-auth-config
                                    code)

             profile-pic-url       (get-in facebook-user [:picture :data :url])

             facebook-user-id      (:id facebook-user)

             base-user             (if-let [user (database/find-entity
                                                  (config/current-database)
                                                  :users
                                                  {:user.auth.facebook/user-id facebook-user-id})]
                                     ;; Existing entity in database
                                     user

                                     ;; New base user entity
                                     {:entity/id (database/new-entity-id)
                                      :user.auth.facebook/user-id facebook-user-id})

             {cloudinary-secure-url
              :secure-url

              cloudinary-public-id
              :public-id

              cloudinary-version
              :version

              cloudinary-format
              :format}              (integration.cloudinary/upload-image-using-image-url
                                     config/default-cloudinary-config
                                     profile-pic-url)

             {:keys [first-name
                     last-name
                     email]}       facebook-user

             existing-user         (database/find-entity
                                    (config/current-database)
                                    :users
                                    {:user.auth.facebook/user-id facebook-user-id})

             user-details          (merge base-user
                                          ;; This will always update the users details
                                          ;; from facebook
                                          {;; Profile details pulled from facebook
                                           :user/first-name first-name
                                           :user/last-name last-name
                                           :user/full-name (str first-name " " last-name)
                                           :user/email-address email
                                           :user/profile-pic-url cloudinary-secure-url

                                           ;; cloudinary integration details
                                           :user.profile-pic.intergration.cloudinary/url cloudinary-secure-url
                                           :user.profile-pic.intergration.cloudinary/public-id cloudinary-public-id
                                           :user.profile-pic.intergration.cloudinary/version cloudinary-version
                                           :user.profile-pic.intergration.cloudinary/format cloudinary-format})

             ;; Always save the user with the latest details
             saved?             (database/save-entity (config/current-database)
                                                      :users
                                                      user-details)

             user-id            (:entity/id user-details)]

         (response/redirect (urls/user-dashboard-route user-id))))


  (GET (urls/user-dashboard-route ":user-id") [user-id]
       (let [user-details (database/find-entity-by-id
                           (config/current-database)
                           :users user-id)]

         (html/render (user-pages/dashboard-page user-details))))


  ;; General 404 / page not found handler,
  ;; Note the 500 / unexpected error handler is implemented as middleware
  ;; in `technical-assessment.middleware/wrap-exceptions`.
  (route/not-found (html/render
                    (general-pages/not-found-page))))


(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      (wrap-exceptions)))
