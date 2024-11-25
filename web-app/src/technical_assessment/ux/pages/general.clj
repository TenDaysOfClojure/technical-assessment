(ns technical-assessment.ux.pages.general
  (:require [technical-assessment.ux.layouts :as layouts]
            [technical-assessment.ux.svg-library :as svg-library]
            [technical-assessment.urls :as urls]))

(defn not-found-page []
  (layouts/main-layout
   [:div.info-alert
    svg-library/info-icon
    "It looks like the page you're looking for doesn't exist or has been moved."]))


(defn unexpected-error-page []
  (layouts/main-layout
   [:div.error-alert
    svg-library/warning-icon
    [:strong "We’re sorry, but something unexpected happened"]
    ". Our team has been notified and is working to fix the problem."]))
