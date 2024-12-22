(ns technical-assessment.logging
  (:require [clojure.string :as string]
            [taoensso.telemere :as logger]
            [clojure.pprint]))

;; -- Settings getters / setters --

(defonce settings (atom {:ansi-colours-enabled? false}))


(defn enable-ansi-terminal-colours! []
  (swap! settings assoc :ansi-colours-enabled? true))


(defn enable-ansi-terminal-colours? []
  (:ansi-colours-enabled? @settings))


;; -- Internal helpers --

;; See https://en.wikipedia.org/wiki/ANSI_escape_code
(defn- ansi-escape-code [code]
  (str "\033[" code "m"))


(defn- reset-ansi-code []
  (ansi-escape-code 0))


(defn highlight-text [colour & text]
  (let [effective-text (string/join " " (flatten text))]
    (if (enable-ansi-terminal-colours?)
      (str (ansi-escape-code colour) effective-text (reset-ansi-code))
      effective-text)))


;; See https://en.wikipedia.org/wiki/ANSI_escape_code#Colors

(def grey-text-code 30)
(def red-text-code 31)
(def green-text-code 32)
(def yellow-text-code 33)
(def blue-text-code 34)
(def magenta-text-code 35)
(def cyan-text-code 36)
(def white-text-code 37)

(defn grey-text [& text]
  (highlight-text grey-text-code text))

(defn red-text [& text]
  (highlight-text red-text-code text))

(defn green-text [& text]
  (highlight-text green-text-code text))

(defn yellow-text [& text]
  (highlight-text yellow-text-code text))

(defn blue-text [& text]
  (highlight-text blue-text-code text))

(defn magenta-text [& text]
  (highlight-text magenta-text-code text))

(defn cyan-text [& text]
  (highlight-text cyan-text-code text))

(defn white-text [& text]
  (highlight-text white-text-code text))


(def grey-background-code 40)
(def red-background-code 41)
(def green-background-code 42)
(def yellow-background-code 43)
(def blue-background-code 44)
(def magenta-background-code 45)
(def cyan-background-code 46)
(def white-background-code 47)


(defn grey-background [& text]
  (highlight-text grey-background-code text))


(defn red-background [& text]
  (highlight-text red-background-code text))


(defn green-background [& text]
  (highlight-text green-background-code text))


(defn yellow-background [& text]
  (highlight-text yellow-background-code text))


(defn blue-background [& text]
  (highlight-text blue-background-code text))


(defn magenta-background [& text]
  (highlight-text magenta-background-code text))


(defn cyan-background [& text]
  (highlight-text cyan-background-code text))


(defn white-background [& text]
  (highlight-text white-background-code text))


(defn enable-minimal-logging! []
  ;; A minimal, spacious logger ideal for development
  (let [output (fn [{:keys [level msg_ error data]}]
                 (let [message (str "[" (name level) "] "

                                    (str (force msg_) "\n")

                                    (when error
                                      (str "\nERROR: " (.getMessage error) "\n\n"

                                       (->> (.getStackTrace error)
                                            (interpose "\n")
                                            (apply str))
                                       "\n"))

                                    (when data
                                      (str "\nDATA: "
                                           (with-out-str
                                             (clojure.pprint/pprint data))))
                                    "\n")]

                   (if (= :error level)
                     ;; Helpful in local development to highlight errors in red
                     ;; in the terminal
                     (red-text message)
                     message)))]

    ;; Remove minimim logging level for development
    (logger/set-min-level! nil)

    ;; Override the default console/terminal logging handler with our custom logging output
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
