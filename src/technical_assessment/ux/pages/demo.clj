(ns technical-assessment.ux.pages.demo
  (:require [technical-assessment.ux.layouts :as layout]
            [technical-assessment.ux.components.common :as common]))


(defn story-book []

  (layout/main-layout

   [:div.space-y-4
    [:h2.text-2xl
     "Alerts"]

    [:h3.text-lg "Warning alert with icon, title and message"]

    (common/warning-alert
     :title "Welcome to the demo page!"
     :message "This is a demo page that showcases the various UI components and layouts used in the application.")

    [:h3.text-lg "Warning alert with icon and message"]

    (common/warning-alert
     :message "This is a demo page that showcases the various UI components and layouts used in the application.")

    [:h3.text-lg "Warning alert with message only"]

    (common/warning-alert
     :icon? false
     :message "This is a demo page that showcases the various UI components and layouts used in the application.")

    [:hr.horizontal-line]

    [:h3.text-lg "Error alert with icon, title and message"]

    (common/error-alert
     :title "Welcome to the demo page!"
     :message "This is a demo page that showcases the various UI components and layouts used in the application.")

    [:h3.text-lg "Error alert with icon and message"]

    (common/error-alert
     :message "This is a demo page that showcases the various UI components and layouts used in the application.")

    [:h3.text-lg "Error alert with message only"]

    (common/error-alert
     :icon? false
     :message "This is a demo page that showcases the various UI components and layouts used in the application.")]))
