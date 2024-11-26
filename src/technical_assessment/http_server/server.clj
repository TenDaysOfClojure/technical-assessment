(ns technical-assessment.http-server.server
  (:require [taoensso.timbre :as logger]
            [ring.adapter.jetty :as jetty]
            [technical-assessment.http-server.routes :as http-routes]))


(defonce http-servers (atom {}))


(defn get-http-server [port]
  (get @http-servers (if (string? port)
                       (Integer/parseInt port)
                       port)))


(defn http-server-running? [port]
  (true? (:running? (get-http-server port))))


(defn start-server! [& {:keys [port] :as options}]
  (if (not (http-server-running? port))
    (do
      (logger/info "Starting HTTP server with options" options)

      (let [server (jetty/run-jetty #'http-routes/app
                                    {:port (if (string? port) (Integer/parseInt port) port)
                                     :join? false
                                     :send-server-version? false})]

        (swap! http-servers assoc port (assoc options
                                              :running? true
                                              :server server))))

    ;; HTTP server is already running, not starting.
    (logger/warn "HTTP server already running with options" options)))


(defn stop-server! [port]
  (if-let [config (get-http-server port)]

    (when (:running? config)
      (let [current-server (:server config)]
        (.stop current-server)
        (.join current-server)

        (swap! http-servers assoc port (-> config
                                           (dissoc :server)
                                           (assoc :running? false)))

        (logger/info "HTTP server stopped on port" port)))

    ;; No server running, not stopping.
    (logger/warn "No HTTP server running on port" port " to stop")))


(defn restart-server! [port]
  (stop-server! port)

  (apply start-server! (get-http-server port)))


(defn stop-all-servers! []
  (doseq [port (keys @http-servers)]
    (stop-server! port)))
