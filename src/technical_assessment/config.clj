(ns technical-assessment.config
  (:require [clojure.string :as string]
            [clj-http.client :as http]
            [camel-snake-kebab.core :as key-transforms]
            [technical-assessment.database.xtdb :as database.xtdb]
            [technical-assessment.logging :as logging])

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
       (ex-info (str "Configuration error: " config-type
                     " config is missing config values."
                     " All values are expected to be provided.")
                {:blank-config-keys blank-keys})))))

;; -- Environment helpers --

(defn production-environment? []
  (= "true" (System/getenv "PRODUCTION_ENV")))


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


(def default-http-port (if-let [http-port (System/getenv "HTTP_PORT")]
                         (Integer/parseInt http-port)
                         ;; Defaults HTTP port to 3000
                         3000))

;; For: clj-http.client
;; register your own body coercers by participating in the coerce-response-body multimethod
;; dispatch to it by using {:as :json-kebab-keys} as an argument to http client calls

;; The below uses camel-snake-kebab library to turn a camel-cased JSON API into
;; idiomatic kebab-cased keywords in clojure data structures and is much
;; faster than applying via postwalk or similar

(defmethod http/coerce-response-body :json-kebab-keys [req resp]
  (http/coerce-json-body req resp (memoize key-transforms/->kebab-case-keyword) false))


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


;; -- Database --

(def xtdb-node-type (or (System/getenv "XTDB_NODE_TYPE")
                        "in-process"))


(defn current-database []
  (if (development-environment?)
    (database.xtdb/get-db xtdb-node-type)
    ;; Only development database is supported in v1
    (throw
     (Exception. "Production database not yet implemented." {}))))


;; -- Config setup --

(defn setup-config! []
  (when (development-environment?)
    (do
      (logging/enable-ansi-terminal-colours!)
      (logging/enable-development-logging!)))

  (check-facebook-auth-config!)
  (check-cloudinary-config!))
