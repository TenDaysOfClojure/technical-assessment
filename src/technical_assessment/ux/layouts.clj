(ns technical-assessment.ux.layouts
  (:require [technical-assessment.config :as config]))


(defn main-layout [children]
  [:html
   [:head
    [:meta {:charset "UTF-8"}]
    [:meta {:content "width=device-width, initial-scale=1.0"
            :name "viewport"}]
    [:link {:rel "stylesheet" :href (config/main-css-path)}]]

   [:body.main-body children]])
