(ns technical-assessment.ux.pages.home
  (:require [technical-assessment.ux.layouts :as layouts]
            [technical-assessment.ux.svg-library :as svg-library]
            [technical-assessment.urls :as urls]))


(defn page []
  (layouts/main-layout
   [:div.space-y-4

    [:div
     [:h1.text-3xl "Welcome to the Technical Assessment"]]

    [:div
     [:p "The objective of this assessment is to:"

      [:ul.list-disc.list-inside
       [:li
        [:strong "Create a Clojure-based web app"]
        " that allows users to sign up via Facebook"]

       [:li
        [:strong "Retrieve the facebook user details "]
        "(first name, last name, email, and profile picture), and "
        [:strong "save to a database."]]

       [:li
        "The " [:strong "profile picture should be retrieved from Facebook" ]
        " and " [:strong "uploaded to Cloudinary,"]]

       [:li
        "The" [:strong " Cloudinary URL of the profile picure" ]
        " should be " [:strong "stored in the database." ]]]]]

    [:hr.horizontal-line]

    [:div.space-y-4

     [:p "If you don't have an account you can sign up now:"]

     [:h2.text-xl "Sign up with Facebook" [:small " (this is your first time here)"]]
     [:a.facebook-login-button.text-sm.w-64
      {:href (urls/auth-facebook-route :sign-up)}
      technical-assessment.ux.svg-library/facebook-icon
      [:span.font-bold.pr-2.text-base"Sign up"] " with Facebook"]

     [:p "If you have an account login to proceed:"]

     [:h2.text-xl "Login with Facebook"]

     [:a.facebook-login-button.text-sm.bg-blue-700.w-64
      {:href (urls/auth-facebook-route :login)}
      technical-assessment.ux.svg-library/facebook-icon
      [:span.font-bold.pr-2.text-base "Log in "] " with Facebook"]]]))