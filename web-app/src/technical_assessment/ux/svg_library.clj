(ns technical-assessment.ux.svg-library)


(defn with-svg-defaults [attributes]
  (assoc attributes
         :xmlns "http://www.w3.org/2000/svg"
         :fill "currentColor"))


(defn with-standard-icon-size [attributes]
  (assoc attributes
         :width "30px"
         :height "30px"))


(defn path-element [path]
  [:path {:d path }])


(defn svg [attributes path]
  [:svg.mr-3 attributes (path-element path)])


;; -- Icon libarary --

(def facebook-icon
  (svg
   (-> {:viewBox "0 0 320 512"}
       (with-svg-defaults)
       (with-standard-icon-size))

   "M80 299.3V512H196V299.3h86.5l18-97.8H196V166.9c0-51.7 20.3-71.5 72.7-71.5c16.3 0 29.4 .4 37 1.2V7.9C291.4 4 256.4 0 236.2 0C129.3 0 80 50.5 80 159.4v42.1H14v97.8H80z"))


(def info-icon
  (svg
   (-> {:viewBox "0 0 512 512"}
       (with-svg-defaults)
       (with-standard-icon-size))

   "M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zM216 336l24 0 0-64-24 0c-13.3 0-24-10.7-24-24s10.7-24 24-24l48 0c13.3 0 24 10.7 24 24l0 88 8 0c13.3 0 24 10.7 24 24s-10.7 24-24 24l-80 0c-13.3 0-24-10.7-24-24s10.7-24 24-24zm40-208a32 32 0 1 1 0 64 32 32 0 1 1 0-64"))


(def warning-icon
  (svg
   (-> {:viewBox "0 0 576 512"}
       (with-svg-defaults)
       (with-standard-icon-size))

   "M569.5 440C588 472 564.8 512 527.9 512H48.1c-36.9 0-60-40.1-41.6-72L246.4 24c18.5-32 64.7-32 83.2 0l239.9 416zM288 354c-25.4 0-46 20.6-46 46s20.6 46 46 46 46-20.6 46-46-20.6-46-46-46zm-43.7-165.3l7.4 136c.3 6.4 5.6 11.3 12 11.3h48.5c6.4 0 11.6-5 12-11.3l7.4-136c.4-6.9-5.1-12.7-12-12.7h-63.4c-6.9 0-12.4 5.8-12 12.7z"))
