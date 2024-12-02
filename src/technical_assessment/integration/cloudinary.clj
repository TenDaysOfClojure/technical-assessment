(ns technical-assessment.integration.cloudinary
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [clojure.pprint]
            [technical-assessment.config :as config]
            [camel-snake-kebab.core :as casing]
            [clojure.string :as string]))


(defn secure-url [{:keys [secure-url]}]
  secure-url)


(defn public-id [{:keys [public-id]}]
  public-id)


(defn version [{:keys [version]}]
  version)


(defn file-format [{:keys [format]}]
  format)


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
                                              cloudinary-config))}}

        response    (client/post url params)

        result-info (json/decode (:body response)
                                 casing/->kebab-case-keyword)]
    result-info))
