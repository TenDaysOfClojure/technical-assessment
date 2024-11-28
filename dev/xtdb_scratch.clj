(ns xtdb-scratch
  (:require [xtdb.node :as xtn]
            [xtdb.client :as xtc]
            [xtdb.api :as xt]))


(comment


  ;; Remote XTDB node
  (let [node-address "http://localhost:6543"]
    (with-open [node (xtc/start-client node-address)]
      (xt/status node)

      #_(xt/execute-tx node
                     [[:put-docs :comments {:xt/id (random-uuid), :post-id "my-post-id"}]])


      (xt/q node '(from :comments [xt/id post-id]))

      ))

  ;; In process node
  ;; Local issues:
  ;;  Caused by java.lang.IllegalStateException
  ;; Memory was leaked by query. Memory leaked: (16384) Allocator(ROOT)
  ;;
  ;; Requires
  ;;
  ;;
  (def in-memory-node (xtn/start-node))

  (xt/status in-memory-node)

  (xt/submit-tx in-memory-node
                [[:put-docs :comments {:xt/id (random-uuid),
                                       :post-id "my-post-id-"}]])


  (xt/q in-memory-node '(from :comments [xt/id post-id]))




  )
