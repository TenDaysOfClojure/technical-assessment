(ns technical-assessment.render-html
  (:require [hiccup2.core :as hiccup]))


(defn render [html-structure]
  (str (hiccup/html html-structure)))
