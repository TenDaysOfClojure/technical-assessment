;; This namespace provides a standardised databse interface
;; which supports both a mock database (see `technical-assessment.database.mock-db`)
;; and other database implemenations

(ns technical-assessment.database.core)


(defn new-entity-id []
  (str (java.util.UUID/randomUUID)))


(defn save-entity [db entity-type entity-details]
  (let [handler (:save-entity db)]
    (handler entity-type entity-details)))


(defn find-entity-by-id [db entity-type entity-id]
  (let [handler (:find-entity-by-id db)]
    (handler entity-type entity-id)))


(defn find-all-entities [db entity-type]
  (let [handler (:find-all-entities db)]
    (handler entity-type)))
