(ns technical-assessment.urls)


(def home-route "/")


(def auth-facebook-base-route "/site/auth/facebook")


(defn auth-facebook-route [action]
  (str auth-facebook-base-route "/" (name action)))


(def auth-facebook-call-back-route "/auth/facebook/callback")


(defn user-dashboard-route [url-suffix]
  (str "/user/dashboard/" url-suffix))
