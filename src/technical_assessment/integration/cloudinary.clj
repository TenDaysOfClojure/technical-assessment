(ns technical-assessment.integration.cloudinary
  (:require [clj-http.client :as client]
            [clojure.java.io :as io]
            [taoensso.timbre :as logger]
            [clojure.string :as string]
            [cheshire.core :as json]
            [clojure.pprint]
            [technical-assessment.config :as config]
            [camel-snake-kebab.core :as casing]))


(def auth-http-headers
  {"Authorization"
   (str "Basic " (config/cloudinary-authorisation-string
                  config/default-cloudinary-config))})

;; Add support for this in v2
#_(defn upload-image-from-file
    [cloudinary-config
     file-path
     ;; Optional / variable options which provide defaults
     & {:keys [upload-preset]
        ;; Check Upload Presets in Cloudinary dashboard for more options
        :or {upload-preset "ml_default"} :as options}]
    (let [url    (str "https://api.cloudinary.com/v1_1/"
                      (:cloud-name cloudinary-config) "/image/upload")

          params {:multipart [{:name "file" :content (io/file file-path)}
                              {:name "upload_preset" :content upload-preset}]
                  :headers auth-http-headers}]

      ;; TODO standardise error handling
      (client/post url params)))


(defn public-image-url [cloudinary-config public-id]
  (str "https://res.cloudinary.com/"
       (:cloud-name cloudinary-config)
       "/image/upload/" public-id))


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
   & {:keys [upload-preset]
      ;; Check Upload Presets in Cloudinary dashboard for more options
      :or {upload-preset "ml_default"} :as options}]
  (let [url        (str "https://api.cloudinary.com/v1_1/"
                        (:cloud-name cloudinary-config) "/image/upload")

        params      {:multipart [{:name "file" :content image-url}
                                 {:name "upload_preset" :content upload-preset}]
                     :headers auth-http-headers}

        response    (client/post url params)

        result-info (json/decode (:body response)
                                 casing/->kebab-case-keyword)]
    result-info))


(defn fetch-image [cloudinary-config public-id]
  ;; Note we fetch cloudinary data as a :stream
  ;; to allow elegant handling of data
  (client/get (public-image-url cloudinary-config public-id)
              {:as :stream}))


(defn fetch-image-and-save-to-local-file
  [cloudinary-config public-id file-path-to-save]

  (let [{:keys [body]} (fetch-image cloudinary-config public-id)]
    ;; This saves the stream to a local file and is an elegant
    ;; want of working with file data
    (io/copy body (java.io.File. file-path-to-save))))
