(ns xtdb-scratch
  (:require
            #_[xtdb.client :as xtc]
            [technical-assessment.database.core :as database]
            [technical-assessment.database.xtdb :as xtdb.database]))


(comment


  (def database (xtdb.database/get-db :in-process))


  (def new-entity-id (database/new-entity-id))


  ;; Persistence / Upsert

  (database/save-entity database
                        :comments
                        {:entity/id new-entity-id
                         :post-id "my-post-id"
                         :content "This is a comment"})

  ;; Finders

  (database/find-all-entities database :comments)


  (database/find-entity-by-id database :comments new-entity-id)


  ;; Query (returns a collection of results)

  (database/query database
                  '(from :comments [xt/id post-id]))

  (database/query database
                  '(from :comments [{:xt/id $entity} *])
                  {:args {:entity new-entity-id}})

  ;; Query one (returns a the single, first result of the query)

  (database/query-one database
                      '(from :comments [xt/id post-id]))

  (database/query-one database
                      '(from :comments [{:xt/id $entity} *])
                      {:args {:entity new-entity-id}})

  ;; Remote node
  #_(xtc/start-client node-address)


  )
