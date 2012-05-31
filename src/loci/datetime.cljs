(ns loci.datetime
  (:require [clojure.string :as string]
            [goog.object :as gobject]
            [goog.i18n.DateTimeParse :as gdate-time-parse]
            [goog.i18n.DateTimeFormat :as gdate-time-format]))


(defn- keywordize [s]
  (->> (string/replace s "_" "-")
       string/lower-case
       keyword))

(def ^:private number-formats
  (into {} (map (fn [[k v]] [(keywordize k) v])
                (js->clj gdate-time-format/Format))))

(defn- resolve-pattern [pattern]
  (if (keyword? pattern)
    (get number-formats pattern)
    pattern))


(defn format [date pattern & [tz]]
  "Format a given date, based on the current locale."
  {:pre [(or (number? pattern)
             (keyword? pattern)
             (string? pattern))]}
  (let [formatter (goog.i18n.DateTimeFormat. (resolve-pattern pattern))]
    (.format formatter date tz)))
