;; A very simple EDN based database for rapid prototyping

(ns technical-assessment.database.mock-db
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.pprint]))

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


(defn pretty-write-edn
  [file-path data]
  (spit (str "./resources/" mock-db-resource-sub-directory-name "/" file-path)
        (with-out-str
          (clojure.pprint/write
           data :dispatch clojure.pprint/code-dispatch))))


;; Note this returns a map of functions intead of a CLojure protocal https://clojure.org/reference/protocols
;; Clojure protocals are great but have a high learning curve for juniors
;; and less experienced clojure developers so this provides a good alternative that uses minimal clojure
;; lanage feature
(defn get-db [file-path]
  {:save-entity
   (fn [entity-kind entity]
     (let [db         (read-edn file-path)

           id         (:entity/id entity)

           updated-db (assoc-in db [entity-kind id] entity)]

       (pretty-write-edn file-path updated-db)

       ;; For now always return true
       true))


   :find-entity-by-id
   (fn [entity-kind id]
     (let [db (read-edn file-path)]
       (get-in db [entity-kind id])))


   :find-all-entities
   (fn [entity-type]
     (vals (get (read-edn file-path) entity-type)))})
