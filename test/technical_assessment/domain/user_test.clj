(ns technical-assessment.domain.user-test
  (:require [technical-assessment.domain.user :as domain.user]
            [clojure.test :refer :all]))


(def sample-user
  {:entity/id "22222117-d7a3-117f7-8149-a11111c0afd"
   :user/full-name "Joe Soap"
   :user/last-name "Soap"
   :user/first-name "Joe"
   :user/profile-pic-url
   "https://res.cloudinary.com/dotmx6rou/image/upload/v1732692127/22222117-d7a3-117f7-8149-a11111c0afd.jpg"
   :user/email-address "joesoap@mailinator.com"


   :user.profile-pic.integration.cloudinary/url
   "https://res.cloudinary.com/dotmx6rou/image/upload/v1732692127/22222117-d7a3-117f7-8149-a11111c0afd.jpg",
   :user.profile-pic.integration.cloudinary/version 1732692127
   :user.profile-pic.integration.cloudinary/public-id
   "22222117-d7a3-117f7-8149-a11111c0afd"
   :user.profile-pic.integration.cloudinary/format "jpg"

   :user.auth.facebook/user-id "555212223112123314"})


(deftest user-test

  (testing "email-address"
    (is (= "joesoap@mailinator.com" (domain.user/email-address sample-user))))


  (testing "profile-pic-url"
    (is (= "https://res.cloudinary.com/dotmx6rou/image/upload/v1732692127/22222117-d7a3-117f7-8149-a11111c0afd.jpg"
           (domain.user/profile-pic-url sample-user) )))


  (testing "full-name"
    (is (= "Joe Soap" (domain.user/full-name sample-user) )))


  (testing "has-email-address?"

    (is (true? (domain.user/has-email-address? sample-user)))

    (is (false? (domain.user/has-email-address?
                 (assoc sample-user :user/email-address ""))))

    (is (false? (domain.user/has-email-address?
                 (assoc sample-user :user/email-address " "))))

    (is (false? (domain.user/has-email-address?
                 (assoc sample-user :user/email-address nil))))

    (is (false? (domain.user/has-email-address?
                 (dissoc sample-user :user/email-address))))))
