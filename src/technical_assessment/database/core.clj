;; This namespace provides a standardised database interface without
;; requiring developers to know the details of the underlying database technology.
;;
;; The underlying database technology is XTDB.
(ns technical-assessment.database.core
  (:require [technical-assessment.database.xtdb :as database.xtdb]
            [technical-assessment.database.core :as database]))


(defn new-entity-id []
  (str (java.util.UUID/randomUUID)))


(defn save-entity [db entity-type entity-details]
  (database.xtdb/save-entity db entity-type entity-details))


(defn find-all-entities [db entity-type]
  (database.xtdb/find-all-entities db entity-type))


(defn find-entity-by-id [db entity-type entity-id]
  (database.xtdb/find-entity-by-id db entity-type entity-id))


(defn query
  ([db query-to-run]
   (query db query-to-run {}))

  ([db query-to-run query-params]
   (database.xtdb/query db query-to-run query-params)))


(defn query-one
  ([db query-to-run]
   (query-one db query-to-run {}))

  ([db query-to-run query-params]
   (database.xtdb/query-one db query-to-run query-params)))


(defn entity-exists-by-id?
  [db entity-type entity-id]
  (not (nil? (find-entity-by-id db entity-type entity-id))))


(defn entity-exists-by-query?
  ([db query]
   (not (nil? (query-one db query))))

  ([db query query-params]
   (not (nil? (query-one db query query-params)))))


(defn delete-entity-by-id [db entity-kind entity-id]
  (database.xtdb/delete-by-id db entity-kind entity-id))
