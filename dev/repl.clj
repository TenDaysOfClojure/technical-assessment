(ns repl
  (:require [technical-assessment.http-server.server :as http-server]
            [technical-assessment.config :as config]
            [nrepl.cmdline]))


(defn -main
  "The main entry point for the REPL will load the config, start the HTTP server
   and then start the nREPL server which can be connected to"
  [& args]
  (config/setup-config!)

  (http-server/start-server! :port config/default-http-port)

  (apply nrepl.cmdline/-main args))
