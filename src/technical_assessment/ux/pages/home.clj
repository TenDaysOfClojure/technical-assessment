(ns technical-assessment.ux.pages.home
  (:require [technical-assessment.ux.layouts :as layouts]
            [technical-assessment.ux.svg-library :as svg-library]
            [technical-assessment.urls :as urls]))


(defn page []
  (layouts/main-layout
   [:div.space-y-4

    [:div
     [:h1.text-3xl "Welcome to the Technical Assessment"]]

    [:div.space-y-2
     [:p "The objective of this assessment is to:"]

     [:ul.list-disc.list-inside
      [:li
       [:strong "Create a Clojure-based web app"]
       " that allows users to sign up via Facebook"]

      [:li
       [:strong "Retrieve the facebook user details "]
       "(first name, last name, email, and profile picture), and "
       [:strong "save to a database"]]

      [:li
       "The " [:strong "profile picture should be retrieved from Facebook" ]
       " and " [:strong "uploaded to Cloudinary"]]

      [:li
       "The" [:strong " Cloudinary URL of the profile picure" ]
       " should be " [:strong "stored in the database" ]]]]

    [:hr.horizontal-line]

    [:div
     [:h2.text-xl "Log in"]
     [:p "Already registered? Log in now to get access to your account."]]

    [:div
     [:a.facebook-login-button
      {:href (urls/auth-facebook-route :login)}
      technical-assessment.ux.svg-library/facebook-icon
      [:span.font-bold.pr-1.text-base "Log in "] " with Facebook"]]

    [:hr.horizontal-line]

    [:div
     [:h2.text-xl "Register"]
     [:p "If you don't have an account yet you can sign up now to get access."]]

    [:div
     [:a.facebook-login-button
      {:href (urls/auth-facebook-route :sign-up)}
      technical-assessment.ux.svg-library/facebook-icon
      [:span.font-bold.pr-1.text-base"Sign up"] " with Facebook"]]]))
