(ns technical-assessment.integration.facebook-auth
  (:require
   ;; 3rd party intergation low level tools
   [clj-http.client :as client]
   [cheshire.core :as json]

   ;; General config
   [technical-assessment.config :as config]
   [technical-assessment.urls :as urls]))


(defn fetch-facebook-auth [code]
  (let [token-resp   (client/post
                      "https://graph.facebook.com/v10.0/oauth/access_token"
                      {:form-params {:client_id (:app-id config/facebook-auth-config)
                                     :redirect_uri (:redirect-uri config/facebook-auth-config)
                                     :client_secret (:client-secret config/facebook-auth-config)
                                     :code code}})

        access-token (:access_token (json/parse-string (:body token-resp) true))

        user-resp    (client/get
                      "https://graph.facebook.com/me"
                      {:query-params {:access_token access-token
                                      :fields "id,first_name,last_name,email,picture.width(2048).height(2048)"}})

        user-info     (json/parse-string (:body user-resp) true)]
    user-info))


(defn authenticaiton-redirect-url
  "Returns the URL to redirect the user to for Facebook authentication and includes
  a given `action` which can be `sign-up` or `login` that is passed to the authentication
  callback as the `state` querystring parameter."
  [facebook-auth-config action]
  (str "https://www.facebook.com/v10.0/dialog/oauth?client_id=" (:app-id facebook-auth-config)
       "&redirect_uri=" (:redirect-uri facebook-auth-config) "&scope=email"
       ;; Note we use Facebook login API `state` param to pass the action to take
       ;; The `response_type` querystring must be `code` for this to work.
       "&response_type=code&state=" action))


(defn fetch-facebook-user-details {{:keys[code state]} :params}
  (let [;; Note we use Facebook login API `state` param to pass the action to take
        ;; `state` is the name of the parameter facebook uses to allow including extra data
        ;; for authentication callbacks. `action-to-take` here can be `sign-up` or `login`.
        action-to-take      state

        user-data           (fetch-facebook-auth code)

        profile-pic-url     (get-in user-data [:picture :data :url])

        {:keys [public-id]} (technical-assessment.integration.cloudinary/upload-image-using-image-url
                             config/default-cloudinary-config
                             profile-pic-url)

        user-details        {:user-id (str (java.util.UUID/randomUUID))
                             :profile-pic.integration.cloudinary/public-id public-id
                             :profile-pic/url (cloudinary/public-image-url public-id)}

        ;; TODO mock saving user to database
        ]

    (str action-to-take " user: "
         user-data
         "  "

         user-details

         )))
