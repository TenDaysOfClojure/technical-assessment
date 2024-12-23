(ns technical-assessment.database.xtdb
  (:require [xtdb.node :as xtn]
            [xtdb.client :as xtc]
            [xtdb.api :as xt]
            [clojure.set]
            [xtdb.client :as xtc]))


(defonce in-memory-node (atom nil))


(defn- get-in-memory-node []
  (if-let [running-node @in-memory-node]
    ;; If there is an existing, running node use it.
    running-node

    ;; Otherwise start a new node and store it in the atom
    ;; so it can be reused in further function calls
    (let [node (xtn/start-node)]
      (reset! in-memory-node node)
      node)))


(defonce remote-node (atom nil))


(defn- get-remote-node [node-url]
  (if-let [running-node @remote-node]
    ;; If there is an existing, running node use it.
    running-node

    ;; Otherwise start a new node and store it in the atom
    ;; so it can be reused in further function calls
    (let [node (xtc/start-client node-url)]
      (reset! remote-node node)
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


(defn get-db
  "`database-type` can be either `in-process` or `:remote-node`"
  [node-address]

  (if (= node-address "in-process")
    (get-in-memory-node)

    ;; Assumes remote node
    (get-remote-node node-address)))


(defn save-entity [node entity-kind entity]
  (let [entity-id       (:entity/id entity)

        entity          (->persistable-entity entity)

        {:keys [tx-id]} (xt/execute-tx
                         node
                         [[:put-docs entity-kind entity]])]

    {:tx-id tx-id :entity/id entity-id}))


(defn find-entity-by-id [node entity-kind id]
  (let [table (name entity-kind)

        query (str "select * from " table " where _id = ?")]

    (-> (xt/q node query {:args [id]})
        (first)
        (->domain-entity))))


(defn find-all-entities [node entity-kind]
  (let [table (name entity-kind)

        query (str "select * from " table )]

    (map ->domain-entity
         (xt/q node query))))


(defn query [node query query-params]
  (map ->domain-entity
       (xt/q node query query-params)))


(defn query-one [node query query-params]
  (-> (xt/q node query query-params)
      (first)
      (->domain-entity)))


(defn delete-by-id [node entity-kind entity-id]
  (xt/submit-tx node
    [[:delete {:from entity-kind :bind '[{:xt/id $entity-id}]}
      {:entity-id entity-id}]]))
