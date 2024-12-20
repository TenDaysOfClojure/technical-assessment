(ns technical-assessment.logging
  (:require [clojure.string :as string]
            [technical-assessment.config :as config]
            [taoensso.telemere :as logger]))


(defn- escape-code [code]
  (str "\033[" code "m"))


(defn- end-colour []
  (escape-code 0))


(defn highlight-text [colour & text]
  (let [effective-text (string/join " " (flatten text))]
    
    (if (config/development-environment?)
      (str (escape-code colour) effective-text (end-colour))
      ;; Do not use terminal colour escaping in production, which uses plain text
      ;; logging
      effective-text)))

(def green-console-colour 32)
(def cyan-console-colour 36)

(defn green-text [& text]
  (highlight-text green-console-colour text))


(defn cyan-text [& text]
  (highlight-text cyan-console-colour text))


(defn ->log-message [& text]
  (string/join " " (flatten text)))

(defn info [& text]
  (logger/log! :info (->log-message text)))


(defn debug [& text]
  (logger/log! :debug (->log-message text)))
