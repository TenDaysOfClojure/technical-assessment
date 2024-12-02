(ns technical-assessment.ux.pages.general
  (:require [technical-assessment.ux.layouts :as layouts]
            [technical-assessment.ux.svg-library :as svg-library]
            [technical-assessment.ux.components.common :as common-components]))


(defn not-found-page []
  (layouts/main-layout

   (common-components/warning-alert
    :message "It looks like the page you're looking for doesn't exist or has been moved.")))


(defn unexpected-error-page []
  (layouts/main-layout

   (common-components/error-alert
    :title "We’re sorry, but something unexpected happened."

    :message "Our team has been notified and is working to fix the problem.")))
