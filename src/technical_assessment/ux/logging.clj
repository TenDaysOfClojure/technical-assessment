(ns technical-assessment.ux.logging
  (:require [technical-assessment.logging :as logging]))


(defn ->message [& messages]
  (logging/->log-message (logging/blue-text  "UX:") messages))
