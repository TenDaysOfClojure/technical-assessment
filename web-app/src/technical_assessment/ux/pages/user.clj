(ns technical-assessment.ux.pages.user
  (:require [technical-assessment.ux.layouts :as layouts]
            [technical-assessment.urls :as urls]
            [technical-assessment.ux.svg-library :as svg-library]
            [technical-assessment.domain.user :as domain.user]
            [clojure.pprint]))


(defn user-fields-table [user]
  ;; Sorts the fields by the key of the map
  (let [fields (sort-by key user)]
    [:table.table-auto
     (for [[key value] fields]
       [:tr.border.border-slate-200
        [:td.p-2.text-gray-700.font-semibold (str key)]
        [:td.p-2.text-gray-500 value]])]))


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
          [:div.flex.space-x-2
           [:a.link.flex {:href (str "mailto:" email-address)}
            svg-library/email-icon  email-address]

           [:span " | "]

           [:a.link {:href urls/home-route} "Log out now"]]))]]

    [:div.space-y-3
     [:h2.text-xl "Congratulations, you're now logged in!"]
     [:p "As part of this technical assessment the following user data was persisted:"]

     [:div (user-fields-table user)]

     [:div [:a.link {:href urls/home-route} "Log out now"]]]]))
