(ns technical-assessment.integration.facebook.user)

;; -- Start Field getters --
;;
;; These field getters provide a standard, centralised way to access fields in a map.
;; This prevents developers needing to know the specific key a given field uses
;; and prevents multiple places in the code that use either `get` or `get-in` on a map
;; that represents this domain object.


(defn user-id [{:keys [id]}]
  id)


(defn profile-picture-url [facebook-user]
  (get-in facebook-user [:picture :data :url]))


(defn first-name [{:keys [first-name]}]
  first-name)


(defn last-name [{:keys [last-name]}]
  last-name)


(defn full-name [{:keys [first-name last-name]}]
  (str first-name " " last-name))


(defn email-address [{:keys [email]}]
  email)

;; -- End Field getters --
