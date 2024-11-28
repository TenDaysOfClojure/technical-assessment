(ns technical-assessment.database.xtdb
  (:require [xtdb.node :as xtn]
            [xtdb.client :as xtc]
            [xtdb.api :as xt]
            [clojure.set]))


(defonce server (atom nil))


(defn- get-in-memory-node []
  (if-let [running-node @server]
    ;; If there is an existing, running node use it.
    running-node

    ;; Otherwise start a new node and store it in the atom
    ;; so it can be reused in further function calls
    (let [node (xtn/start-node)]
      (reset! server node)
      node)))


(defn new-database-entity-id []
  (str (random-uuid)))



(defn ->persistable-entity [entity]
  (-> entity
      (update :entity/id (fn [entity-id]
                           (or entity-id (new-database-entity-id))))

      (clojure.set/rename-keys {:entity/id :xt/id})))


(defn ->domain-entity [entity]
  (when entity
    (clojure.set/rename-keys entity {:xt/id :entity/id})))


;; Note this returns a map of functions intead of a CLojure protocal https://clojure.org/reference/protocols
;; Clojure protocals are great but have a high learning curve for juniors
;; and less experienced clojure developers so this provides a good alternative that uses minimal clojure
;; lanage feature

(defn get-db
  "`database-type` can be either `:in-process` or `:remote-node`"
  [database-type]

  (let [node (if (= database-type :in-process)
               (get-in-memory-node)
               ;; TODO - Support remote node in else statement
               )]

    {:save-entity
     (fn [entity-kind entity]
       (let [entity-id       (:entity/id entity)

             entity          (->persistable-entity entity)

             {:keys [tx-id]} (xt/execute-tx
                              node
                              [[:put-docs entity-kind entity]])]

         {:tx-id tx-id :entity/id entity-id}))


     :find-entity-by-id
     (fn [entity-kind id]
       (let [table (name entity-kind)

             query (str "select * from " table " where _id = ?")]

         (-> (xt/q node query {:args [id]})
             (first)
             (->domain-entity))))


     :find-all-entities
     (fn [entity-kind]
       (let [table (name entity-kind)

             query (str "select * from " table )]

         (map ->domain-entity
              (xt/q node query))))


     :query
     (fn [query query-params]
       (prn query query-params)
       (map ->domain-entity
            (xt/q node query query-params)))

     :query-one
     (fn [query query-params]
       (-> (xt/q node query query-params)
           (first)
           (->domain-entity)))}))
