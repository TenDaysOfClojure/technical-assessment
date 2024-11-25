(ns technical-assessment.database.core)


(defn save-entity [db entity-type entity-details]
  (let [handler (:save-entity db)]
    (handler entity-type entity-details)))


(defn find-entity [db entity-type entity-id]
  (let [handler (:find-entity db)]
    (handler entity-type entity-id)))
