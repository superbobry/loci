(ns loci.datetime
  (:require [clojure.string :as string]
            [goog.object :as gobject]
            [goog.i18n.DateTimeParse :as gdate-time-parse]
            [goog.i18n.DateTimeFormat :as gdate-time-format]
            [goog.date.DateTime :as gdate-time]))


(defn- keywordize [s]
  (->> (string/replace s "_" "-")
       string/lower-case
       keyword))

(def ^:private number-formats
  (into {} (map (fn [[k v]] [(keywordize k) v])
                (js->clj gdate-time-format/Format))))

(defn- resolve-pattern [pattern]
  (cond
    (contains? number-formats pattern) (get number-formats pattern)
    (number? pattern) (do (assert (some (fn [[k v]] (== v pattern))
                                        number-formats)
                                  (str "Invalid number format: " pattern))
                          pattern)
    :else (name pattern)))


(defn format [date pattern & [tz]]
  "Format a given date, based on the current locale."
    {:pre [(or (number? pattern)
               (keyword? pattern)
               (string? pattern))]}
  (let [formatter (goog.i18n.DateTimeFormat. (resolve-pattern pattern))]
    (.format formatter date tz)))
    
(defn parse [string pattern]
  "Parse string, using given pattern "
  {:pre [(or (number? pattern)
             (keyword? pattern)
             (string? pattern))]}
  (let [parser (goog.i18n.DateTimeParse (resolve-pattern pattern))
        date (goog.i18n.DateTime.)]
    (.parse parser date)
    date))
