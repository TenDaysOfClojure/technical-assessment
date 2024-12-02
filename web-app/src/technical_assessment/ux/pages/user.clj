(ns technical-assessment.ux.pages.user
  (:require [technical-assessment.ux.layouts :as layouts]
            [technical-assessment.urls :as urls]
            [technical-assessment.ux.svg-library :as svg-library]
            [technical-assessment.domain.user :as domain.user]))

(defn dashboard-page [user]
  (layouts/main-layout

   [:div.space-y-5

    [:div.flex.items-center.content-start.space-x-4

     [:div
      [:img.rounded-full.w-28.border
       {:src (domain.user/profile-pic-url user)
        :alt "Profile picture"}]]

     [:div.space-y-2
      [:h1.text-3xl "👋 Welcome " (domain.user/full-name user) "!"]

      (when (domain.user/has-email-address? user)
        (let [email-address (domain.user/email-address user)]
          [:div
           [:a.link.flex {:href (str "mailto:" email-address)}
            svg-library/email-icon  email-address]]))]]

    [:div
     [:h2.text-xl "Congratulations, you're now logged in!"]
     [:p "This completes the technical assessment, thank you for your time."]]

    [:a.link {:href urls/home-route} "Log out now"]]))
