(ns technical-assessment.ux.pages.user
  (:require [technical-assessment.ux.layouts :as layouts]
            [technical-assessment.urls :as urls]
            [technical-assessment.ux.svg-library :as svg-library]
            [technical-assessment.domain.user :as domain.user]
            [clojure.pprint]
            [clojure.string :as string]))


(defn pretty-database-entity [entity]
  [:pre.text-xs.md:text-sm.font-mono.whitespace-pre-line
   (string/replace (with-out-str
                     (clojure.pprint/pprint entity))
                   #"," "")])


(defn dashboard-page [user]
  (layouts/main-layout

   (if (not (nil? user))

     [:div.space-y-5

      [:div.text-center.space-y-3.md:flex.md:items-center.md:content-start.md:space-x-4

       [:div.mx-auto.md:m-0
        [:img.rounded-full.w-28.border.mx-auto
         {:src (domain.user/profile-pic-url user)
          :alt "Profile picture"}]]

       [:div.space-y-2.text-center
        [:h1.text-3xl "👋 Welcome " (domain.user/full-name user) "!"]

        (when (domain.user/has-email-address? user)
          (let [email-address (domain.user/email-address user)]
            [:div.space-x-2.text-center
             [:a.link {:href (str "mailto:" email-address)}
              svg-library/email-icon  email-address]

             [:span " | "]

             [:a.link {:href urls/home-route} "Log out now"]]))]]

      #_[:div.space-y-3.mx-auto.w-fit.md:w-full
       [:h2.text-xl "Congratulations, you're now logged in!"]
       [:p "As part of this technical assessment the following user data was persisted:"]

       [:div (pretty-database-entity user)]

       [:div [:a.link {:href urls/home-route} "Log out now"]]]]

     ;; No user provided
     [:div.warning-alert
      svg-library/info-icon

      [:div "We cannot display the user dashboard as no user data was provided."]])))
