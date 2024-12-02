(ns technical-assessment.domain.user
  (:require [clojure.string :as string]))


(defn email-address [{:keys [user/email-address]}]
  email-address)


(defn profile-pic-url [{:keys [user/profile-pic-url]}]
  profile-pic-url)


(defn full-name [{:keys [user/full-name]}]
  full-name)


(defn has-email-address? [user]
  (not (string/blank? (email-address user))))
