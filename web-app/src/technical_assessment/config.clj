(ns technical-assessment.config
  (:require [clojure.string :as string]
            [taoensso.timbre :as logger]
            [clojure.pprint])

  (:import java.util.Base64))

;; -- Config helpers --

(defn- check-config-map!
  "Checks the given `config-map` and throws an exception if any of the values
  are blank. The exception includes a list of the keys with blank values."
  [config-type config-map]
  (when (not (every? #(not (string/blank? %)) (vals config-map)))
    (let [blank-keys (->> config-map
                          (filter (fn [[_ config-value]]
                                    (string/blank? config-value)))
                          (map first))]
      (throw
       (ex-info (str "Configuration error: " config-type " config is missing config values."
                     " All values are expected to be provided.")
                {:blank-config-keys blank-keys})))))

;; -- Logger config --

;; A minimal, spacious logger ideal for development
(defn enable-minimal-logging! []
  (let [safe-println (fn [& more]
                       (.write *out* (str (string/join " " more) "\n"))
                       (flush))]
    (logger/set-config!
     {:level :debug
      :appenders {"minimal"
                  {:enabled? true
                   :async? false
                   :min-level nil
                   :fn
                   (fn [{:keys [?err  level timestamp_ vargs]}]
                     (safe-println (str @timestamp_ " "
                                        (string/upper-case (name level)) ":")
                                   (string/join " " vargs))

                     (if (not (nil? ?err))
                       (clojure.pprint/pprint ?err))

                     (safe-println "\n"))}}})))

;; -- Environment helpers --

(defn production-environment? []
  (= "true" (System/getenv "PROD")))


(defn development-environment? []
  (not (production-environment?)))


;; -- HTTP and HTML config --

(defn main-css-path
  "Returns the path to the main CSS file. In development, the path includes a
  query parameter with the current time to prevent caching."
  []
  (let [css-path "/css/main.css"]
    (if (development-environment?)
      (str css-path "?version=" (System/currentTimeMillis))
      css-path)))


(def default-http-port 3000)


;; -- Facebook auth config --

(def facebook-auth-config
  {:app-id (System/getenv "FACEBOOK_AUTH_APP_ID")
   :client-secret (System/getenv "FACEBOOK_AUTH_SECRET")
   :redirect-uri (System/getenv "FACEBOOK_AUTH_REDIRECT_URL")})


(defn check-facebook-auth-config! []
  (check-config-map! "Facebook Auth" facebook-auth-config))


;; -- Couldinary Config --

(defn- base-64-encode-string [to-encode]
  (.encode (Base64/getEncoder) (.getBytes to-encode)))


(defn cloudinary-authorisation-string [{:keys [api-key api-secret]}]
  (base-64-encode-string (str api-key ":" api-secret)))


(def default-cloudinary-config
  {:cloud-name (System/getenv "CLOUDINARY_CLOUD_NAME")
   :api-key (System/getenv "CLOUDINARY_API_KEY")
   :api-secret (System/getenv "CLOUDINARY_API_SECRET")})


(defn check-cloudinary-config! []
  (check-config-map! "Cloudinary" default-cloudinary-config))


(defn setup-config! []
  (enable-minimal-logging!)

  (check-facebook-auth-config!)
  (check-cloudinary-config!))
