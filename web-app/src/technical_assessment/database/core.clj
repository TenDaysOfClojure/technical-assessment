(ns technical-assessment.database.core)


(defn save-entity [db entity-type entity-details]
  (let [save-entity-handler (:save-entity db)]
    (save-entity-handler entity-type entity-details)))
