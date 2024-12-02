;; A very simple EDN based database for rapid prototyping
(ns technical-assessment.database.mock-db
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]))

(def mock-db-resource-sub-directory-name "mock_database")

(defn- read-edn [file-path]
  (let [resource-path (io/resource
                       (str mock-db-resource-sub-directory-name "/" file-path))]
    (if (not (nil? resource-path))
      (edn/read-string
       (slurp
        (io/resource
         (str mock-db-resource-sub-directory-name "/" file-path))))

      ;; Database file does not exist yet, return empty database
      {})))


(defn- write-edn [file-path data]
  (spit
   (str "./resources/"
        mock-db-resource-sub-directory-name
        "/" file-path)

   (prn-str data)))


(defn get-db [file-path]
  {:save-entity
   (fn [entity-kind entity]
     (let [db         (read-edn file-path)

           id         (:entity/id entity)

           updated-db (assoc-in db [entity-kind id] entity)]
       (write-edn file-path updated-db)

       ;; For now always return true
       true))

   :update-entity
   (fn [id updates]
     (let [db (read-edn file-path)]
       (if-let [entity (get db id)]
         (let [updated-entity (merge entity updates)
               updated-db (assoc db id updated-entity)]
           (write-edn file-path updated-db)
           updated-entity)
         (throw (ex-info "Entity not found" {:id id})))))

   :find-entity
   (fn [entity-kind id]
     (let [db (read-edn file-path)]
       (get-in db [entity-kind id])))

   :find-all-entities
   (fn []
     (vals (read-edn file-path)))})
