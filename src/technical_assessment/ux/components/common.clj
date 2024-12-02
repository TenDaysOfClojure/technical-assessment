(ns technical-assessment.ux.components.common
  (:require [technical-assessment.ux.svg-library :as svg-library]
            [clojure.string :as string]))


(defn warning-alert [& {:keys [icon? title message]
                        :or {icon? true}}]
  [:div.warning-alert
   (when icon? svg-library/info-icon)

   (when (not (string/blank? title))
     [:div.font-semibold title])

   [:div message]])


(defn error-alert [& {:keys [icon? title message]
                        :or {icon? true}}]
  [:div.error-alert
   (when icon? svg-library/warning-icon)

   (when (not (string/blank? title))
     [:div.font-semibold title])

   [:div message]])
