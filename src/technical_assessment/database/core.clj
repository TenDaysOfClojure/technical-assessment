;; This namespace provides a standardised databse interface
;; which supports both a mock database (see `technical-assessment.database.mock-db`)
;; and other database implemenations

;; Subsequent versions will provide a https://xtdb.com provider

(ns technical-assessment.database.core
  (:require [taoensso.timbre :as logger]))


(defn new-entity-id []
  (str (java.util.UUID/randomUUID)))


(defn save-entity [db entity-type entity-details]
  (let [handler (:save-entity db)]
    (handler entity-type entity-details)))


(defn find-all-entities [db entity-type]
  (let [handler (:find-all-entities db)]
    (handler entity-type)))


(defn find-entity-by-id [db entity-type entity-id]
  (let [handler (:find-entity-by-id db)]
    (handler entity-type entity-id)))


(defn query
  ([db query-to-run]
   (query db query-to-run {}))

  ([db query-to-run query-params]
   (let [handler (:query db)]
     (handler query-to-run query-params))))


(defn query-one
  ([db query-to-run]
   (query-one db query-to-run {}))

  ([db query-to-run query-params]
   (let [handler (:query-one db)]
     (logger/debug "XTDB query-one:" query-to-run query-params)

     (handler query-to-run query-params))))


(defn entity-exists-by-id?
  [db entity-type entity-id]
  (not (nil? (find-entity-by-id db entity-type entity-id))))


(defn entity-exists-by-query?
  ([db query]
   (not (nil? (query-one db query))))

  ([db query query-params]
   (not (nil? (query-one db query query-params)))))
