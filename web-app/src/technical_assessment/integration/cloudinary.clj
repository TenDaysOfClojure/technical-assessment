(ns technical-assessment.integration.cloudinary
  (:require [clj-http.client :as client]
            [clojure.java.io :as io]
            [taoensso.timbre :as logger]
            [clojure.string :as string]
            [cheshire.core :as json]
            [clojure.pprint]
            [technical-assessment.config :as config]))


(def auth-http-headers
  {"Authorization"
   (str "Basic " (config/cloudinary-authorisation-string
                  config/default-cloudinary-config))})

#_(defn upload-image [cloudinary-config
                    image-path
                    ;; Optional / variable options which provide defaults
                    & {:keys [upload-preset]
                       ;; Check Upload Presets in Cloudinary dashboard for more options
                       :or {upload-preset "ml_default"} :as options}]
  (let [url    (str "https://api.cloudinary.com/v1_1/"
                    (:cloud-name cloudinary-config) "/image/upload")

        params {:multipart [{:name "file" :content (io/file image-path)}
                            {:name "upload_preset" :content upload-preset}]
                :headers auth-http-headers}]

    ;; TODO standardise error handling
    (client/post url params)))


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

        result-info (json/decode (:body response))]

    {:public-id (get result-info "public_id")}))


(defn fetch-image [cloudinary-config public-id]
  (let [url (str "https://res.cloudinary.com/"
                 (:cloud-name cloudinary-config)
                 "/image/upload/" public-id)]
    ;; Note we fetch cloudinary data as a :stream
    ;; to allow elegant handling of data
    ;;
    (client/get url {:as :stream})))


(defn fetch-image-and-save-to-file
  [cloudinary-config public-id file-path-to-save]

   ;; TODO error handling
  (let [{:keys [body]} (fetch-image cloudinary-config public-id)]
    ;; This saves the stream to a local file
    (io/copy body (java.io.File. file-path-to-save))))


(def default-cloudinary-config
  {:cloud-name (System/getenv "CLOUDINARY_CLOUD_NAME")
   :api-key (System/getenv "CLOUDINARY_API_KEY")
   :api-secret (System/getenv "CLOUDINARY_API_SECRET")})


(comment

  ;; Upload single file from url
  #_(let [upload-response (upload-image-using-image-url
                         default-cloudinary-config
                         "https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=122124092180403314&height=2048&width=2048&ext=1735114996&hash=AbbJXR9FEcl9dxDuav-Q6P35")

        _ (clojure.pprint/pprint upload-response)]

    {:public-id "profilepic_pnq4po"}
    true)


  ;; Fetch and save single file
  (let [public-id        "test-image_px6tzb" #_"cld-sample-4"
        file-path-to-save "/Users/ghostdog/Desktop/hello-world.jpg"]
    (fetch-image-and-save-to-file default-cloudinary-config
                                  public-id
                                  "/Users/ghostdog/Desktop/hello-world.jpg"))




  )
