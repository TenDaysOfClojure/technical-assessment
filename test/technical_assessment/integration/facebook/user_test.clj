(ns technical-assessment.integration.facebook.user-test
  (:require [clojure.test :refer [deftest is testing]]
            [technical-assessment.integration.facebook.user :as integration.facebook-user]))

;; This is a sample of what is returned by `technical-assessment.integration.facebook.auth/find-facebook-user
;; which converts the JSON response from Facebook into a Clojure map and kebab-cases keywordizes the keys.
(def sample-facebook-user
  {:id "111111222223333"
   :first-name "Joe"
   :last-name "Soap"
   :email "joesoap@mailinator.com"
   :picture
   {:data
    {:height 1724
     :is-silhouette false
     :url
     "https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=111111222223333&height=2048&width=2048&ext=1",
     :width 1724}}})


(deftest facebook-user-test

  (testing "user-id"
    (is (= "111111222223333" (integration.facebook-user/user-id sample-facebook-user))))


  (testing "profile-picture-url"
    (is (= "https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=111111222223333&height=2048&width=2048&ext=1"
           (integration.facebook-user/profile-picture-url sample-facebook-user))))


  (testing "first-name"
    (is (= "Joe" (integration.facebook-user/first-name sample-facebook-user))))


  (testing "last-name"
    (is (= "Soap" (integration.facebook-user/last-name sample-facebook-user))))



  (testing "full-name"
    (is (= "Joe Soap" (integration.facebook-user/full-name sample-facebook-user))))


  (testing "email-address"
    (is (= "joesoap@mailinator.com" (integration.facebook-user/email-address sample-facebook-user)))))
