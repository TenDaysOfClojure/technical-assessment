(ns technical-assessment.integration.facebook.auth
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [camel-snake-kebab.core :as casing]))


(defn find-facebook-user [facebook-auth-config code]
  (let [token-resp    (client/post
                       "https://graph.facebook.com/v21.0/oauth/access_token"
                       {:form-params {:client_id (:app-id facebook-auth-config)
                                      :redirect_uri (:redirect-uri facebook-auth-config)
                                      :client_secret (:client-secret facebook-auth-config)
                                      :code code}})

        access-token  (:access-token (json/parse-string
                                      (:body token-resp)
                                      casing/->kebab-case-keyword))

        fields        "id,first_name,last_name,email,picture.width(2048).height(2048)"

        user-response (client/get
                       "https://graph.facebook.com/me"
                       {:query-params {:access_token access-token
                                       :fields fields}})

        user-details  (json/parse-string (:body user-response)
                                         casing/->kebab-case-keyword)]

    (clojure.pprint/pprint user-details)
    user-details))


(defn authenticaiton-redirect-url
  "Returns the URL to redirect the user to for Facebook authentication and includes
  a given `action` which can be `sign-up` or `login` that is passed to the authentication
  callback as the `state` querystring parameter."
  [facebook-auth-config action]
  (str "https://www.facebook.com/v21.0/dialog/oauth?client_id=" (:app-id facebook-auth-config)
       "&redirect_uri=" (:redirect-uri facebook-auth-config) "&scope=email"
       ;; Note we use Facebook login API `state` param to pass the action to take
       ;; The `response_type` querystring must be `code` for this to work.
       "&response_type=code&state=" action))
