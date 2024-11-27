(ns technical-assessment.integration.cloudinary-test
  (:require [technical-assessment.integration.cloudinary :as integration.cloudinary]
            [clojure.test :refer :all]))


;; This is sample data returned by the `technical-assessment.integration.cloudinary/upload-image-using-image-url` function
;; which converts the JSON response from Facebook into a Clojure map and kebab-cases keywordizes the keys.
(def sample-cloudinary-entity
  {:tags ["user profile picture"],
   :format "jpg",
   :existing true,
   :placeholder false,
   :asset-folder "",
   :version-id "fa4b7d7231f74bd0de4aafc22bd24772",
   :signature "c31bbacd40c7caa72e389f69f40894d0d80a1c27",
   :width 1724,
   :type "upload",
   :etag "6e432f248db20f486c37d2d8feb415da",
   :secure-url
   "https://res.cloudinary.com/das1dd1ou/image/upload/v1732692127/22222117-1111-47f7-8149-312dd1220afd.jpg",
   :url
   "http://res.cloudinary.com/das1dd1ou/image/upload/v1732692127/22222117-1111-47f7-8149-312dd1220afd.jpg",
   :resource-type "image",
   :display-name "22222117-1111-47f7-8149-312dd1220afd",
   :bytes 486901,
   :asset-id "40db200c18a3056702965185c2ce633a",
   :version 1732692127,
   :created-at "2024-11-27T07:22:07Z",
   :height 1724,
   :public-id "22222117-1111-47f7-8149-312dd1220afd"} )


(deftest cloudinary-test

  (testing "secure-url"
    (is (= "https://res.cloudinary.com/das1dd1ou/image/upload/v1732692127/22222117-1111-47f7-8149-312dd1220afd.jpg"
           (integration.cloudinary/secure-url sample-cloudinary-entity))))


  (testing "public-id"
    (is (= "22222117-1111-47f7-8149-312dd1220afd"
           (integration.cloudinary/public-id sample-cloudinary-entity))))


  (testing "version"
    (is (= 1732692127 (integration.cloudinary/version sample-cloudinary-entity))))


  (testing "file-format"
    (is (= "jpg"
           (integration.cloudinary/file-format sample-cloudinary-entity)))))
