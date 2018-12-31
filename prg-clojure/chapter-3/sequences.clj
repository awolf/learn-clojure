(use '[clojure.java.io :only (reader)])
(use '[clojure.string :only (blank?)])
(defn non-blank? [line] (not (blank? line)))

(defn non-svn? [file] (not (.contains (.toString file) ".svn")))

(defn clojure-source? [file] (.endsWith (.toString file) ".clj"))

(defn clojure-loc [base-file]
    (reduce
        +
        (for [file (file-seq base-file)
            :when (and (clojure-source? file) (non-svn? file))]
            (with-open [rdr (reader file)]
                (count (filter non-blank? (line-seq rdr)))))))

(clojure-loc (java.io.File. "."))

(require [clojure.java.io :as io] )
(seq (.listFiles (clojure.java.io/file ​"."​)))