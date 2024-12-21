(ns technical-assessment.logging
  (:require [clojure.string :as string]
            [taoensso.telemere :as logger]
            [clojure.pprint]))

;; -- Settings getters / setters --

(def settings (atom {:ansi-colours-enabled? false}))


(defn enable-ansi-console-colours! []
  (swap! settings assoc :ansi-colours-enabled? true))


(defn enable-ansi-console-colours? []
  (:ansi-colours-enabled? @settings))


;; -- Internal helpers --

;; See https://en.wikipedia.org/wiki/ANSI_escape_code
(defn- ansi-escape-code [code]
  (str "\033[" code "m"))


(defn- reset-ansi-code []
  (ansi-escape-code 0))


(defn highlight-text [colour & text]
  (let [effective-text (string/join " " (flatten text))]
    (if (enable-ansi-console-colours?)
      (str (ansi-escape-code colour) effective-text (reset-ansi-code))
      effective-text)))


;; See https://en.wikipedia.org/wiki/ANSI_escape_code#Colors
(def green-console-colour 32)
(def cyan-console-colour 36)
(def red-console-colour 31)


(defn green-text [& text]
  (highlight-text green-console-colour text))


(defn cyan-text [& text]
  (highlight-text cyan-console-colour text))


(defn enable-minimal-logging! []
  ;; A minimal, spacious logger ideal for development
  (let [output (fn [{:keys [level msg_ error data]}]
                 (let [message (str "[" (name level) "] "

                                    (str (force msg_) "\n")

                                    (when error
                                      (str "\nERROR: " (.getMessage error) "\n\n"
                                           (apply str (interpose "\n" (.getStackTrace error)))
                                           "\n"))

                                    (when data
                                      (str "\nDATA: " (with-out-str
                                                        (clojure.pprint/pprint data))))
                                    "\n")]

                   (if (= :error level)
                     ;; Helpful in local development to highlight errors in red on the console.xk
                     (highlight-text red-console-colour message)
                     message)))]

    ;; Remove minimim logging level for development
    (logger/set-min-level! nil)

    ;; Override the default console logging handler with our custom logging output
    (logger/add-handler!
     :default/console
     (logger/handler:console {:output-fn output}))))


(defn ->log-message [& text]
  (string/join " " (flatten text)))


(defn info [& text]
  (logger/log! :info (->log-message text)))


(defn debug [& text]
  (logger/log! :debug (->log-message text)))


(defn warn [& text]
  (logger/log! :warn (->log-message text)))


(defn error [& {:keys [exception data message]}]
  (logger/log! {:level :error :error exception :data data}
               (->log-message message)))
