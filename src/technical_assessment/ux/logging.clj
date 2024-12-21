(ns technical-assessment.ux.logging
  (:require [technical-assessment.logging :as logging]))


(defn ->ux-log-message [& messages]
  (logging/->log-message "UX: " messages))


(defn ux-log-info
  "Logs an info message with the given `message` and `data`."
  [message & data]
  (apply logging/info message data))


(defn ux-log-debug
  "Logs a debug message with the given `message` and `data`."
  [message & data]
  (apply logging/debug message data))
