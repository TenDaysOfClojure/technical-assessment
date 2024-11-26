(ns technical-assessment.domain.user
  (:require [clojure.string :as string]
            [technical-assessment.integration.cloudinary :as integration.cloudinary]
            [technical-assessment.integration.facebook.auth :as integration.facebook-auth]
            [technical-assessment.database.core :as database]
            [technical-assessment.config :as config]
            [technical-assessment.integration.facebook.user :as integration.facebook-user]))


;; -- Start Field getters --
;;
;; These field getters provide a standard, centralised way to access fields in a map.
;; This prevents developers needing to know the specific key a given field uses
;; and prevents multiple places in the code that use either `get` or `get-in` on a map
;; that represents this domain object.

(defn email-address [{:keys [user/email-address]}]
  email-address)


(defn profile-pic-url [{:keys [user/profile-pic-url]}]
  profile-pic-url)


(defn full-name [{:keys [user/full-name]}]
  full-name)


(defn has-email-address? [user]
  (not (string/blank? (email-address user))))

;; -- End Field getters --


(defn user-details-for-login-or-sign-up [facebook-user cloudinary-result]
  {;; Profile details pulled from facebook
   :user/first-name (integration.facebook-user/first-name facebook-user)

   :user/last-name (integration.facebook-user/last-name facebook-user)

   :user/full-name (integration.facebook-user/full-name facebook-user)

   :user/email-address (integration.facebook-user/email-address facebook-user)

   :user/profile-pic-url (integration.cloudinary/secure-url cloudinary-result)

   ;; cloudinary integration details for reference
   :user.profile-pic.integration.cloudinary/url
   (integration.cloudinary/secure-url cloudinary-result)

   :user.profile-pic.integration.cloudinary/public-id
   (integration.cloudinary/public-id cloudinary-result)

   :user.profile-pic.integration.cloudinary/version
   (integration.cloudinary/version cloudinary-result)

   :user.profile-pic.integration.cloudinary/format
   (integration.cloudinary/file-format cloudinary-result)})


(defn login-or-sign-up-user-via-facebook
  "Logs in or signs up a user via facebook using the given `facebook-auth-code`
  which is provided by the facebook authentication callback."
  [facebook-auth-code]
  (let [facebook-user    (integration.facebook-auth/find-facebook-user
                          config/facebook-auth-config
                          facebook-auth-code)

        facebook-user-id (integration.facebook-user/user-id facebook-user)

        base-user        (if-let [user (database/find-entity
                                        (config/current-database)
                                        :users
                                        {:user.auth.facebook/user-id facebook-user-id})]
                           ;; Existing entity in database
                           user

                           ;; New base user entity
                           {:entity/id (database/new-entity-id)
                            :user.auth.facebook/user-id facebook-user-id})

        profile-pic-url   (integration.facebook-user/profile-picture-url facebook-user)

        cloudinary-result (integration.cloudinary/upload-image-using-image-url
                           config/default-cloudinary-config
                           profile-pic-url
                           :public-id (:entity/id base-user)
                           :tags ["user profile picture"])

        ;; For now this will always update the users details using facebook user
        ;; details and also upload the latest profile picture to cloudinary
        ;; on every login, this can be performance optimised in later versions.

        user-details      (merge
                           base-user
                           (user-details-for-login-or-sign-up facebook-user
                                                              cloudinary-result))

        ;; Always save the user with the latest details
        saved?            (database/save-entity (config/current-database)
                                                :users
                                                user-details)]

    user-details))
