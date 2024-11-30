(ns technical-assessment.integration.cloudinary
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [clojure.pprint]
            [technical-assessment.config :as config]
            [camel-snake-kebab.core :as casing]
            [clojure.string :as string]))

;; -- Start Field getters --
;;
;; These field getters provide a standard, centralised way to access fields in a map.
;; This prevents developers needing to know the specific key a given field uses
;; and prevents multiple places in the code that use either `get` or `get-in` on a map
;; that represents this domain object.

(defn secure-url [{:keys [secure-url]}]
  secure-url)


(defn public-id [{:keys [public-id]}]
  public-id)


(defn version [{:keys [version]}]
  version)


(defn file-format [{:keys [format]}]
  format)

;; -- End Field getters --


(defn upload-image-using-image-url
  [cloudinary-config
   image-url
   ;; Optional / variable options which provide defaults
   & {:keys [public-id upload-preset tags]
      ;; Check Upload Presets in Cloudinary dashboard for more options
      :or {upload-preset "ml_default"} :as options}]

  (let [url        (str "https://api.cloudinary.com/v1_1/"
                        (:cloud-name cloudinary-config) "/image/upload")

        api-params (cond-> [{:name "file" :content image-url}
                            {:name "upload_preset" :content upload-preset}]

                     ;; Optionally inlcude a `public-id` if provided
                     ;; this will allow multiple uploads and overwrite
                     ;; the image instead of creating new ones
                     (not (nil? public-id))
                     (conj {:name "public_id" :content public-id})

                     (not (empty? tags))
                     (conj {:name "tags" :content (string/join "," tags)}))

        params      {:multipart api-params
                     :headers {"Authorization"
                               (str "Basic " (config/cloudinary-authorisation-string
                                              cloudinary-config))}
                     :as :json-kebab-keys}

        response    (client/post url params)]
    (:body response)))
