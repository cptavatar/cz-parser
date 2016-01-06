(ns parse
  (:gen-class)
  (:require [cheshire.core :as json]))

(def testdata (json/parse-stream (clojure.java.io/reader "/Users/rosea/Downloads/zoomrank/single.txt") true ))

(defn must-contain
  [sub line]
  (if (.contains line sub)
    line
    nil))

(defn find-local-indented
  [node]
  (if
   ;(and
    ;  (.equals "local" (:type node) )
      (contains? node :indented_results)

    (println json/generate-string node)
    ))

(defn process-json
  [node matcher]
  (println (count (:serps node)))
  (doall (map matcher (:serps node))))

(defn process-line
  "For a given JSON record, "
  [pre-processor matcher line]
  (let [post-line (pre-processor line)]
    (if (nil? post-line)
      nil
      (process-json
        (json/parse-string post-line true)
        matcher ))))


(defn process-file
  "For a given filename and row processing function, return a collection based on the results"
  ([filename pre-processor matcher]
   (with-open [rdr (clojure.java.io/reader filename)]
     ;; line-seq is lazy, need to force eval
     (doall (map
              (partial process-line pre-processor matcher)
              (line-seq rdr)
              )))))






(defn -main
  "Application entry point"
  [& args]
  (process-file "/Users/rosea/Downloads/zoomrank/16_20160104_results.txt"
                (partial must-contain "\"local\"")
                find-local-indented
                )
  )