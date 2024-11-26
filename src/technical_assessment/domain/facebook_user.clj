(ns technical-assessment.domain.facebook-user)


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
