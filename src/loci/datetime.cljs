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
  {:pre [(or (number? pattern)
             (keyword? pattern)
             (string? pattern))]}
  (cond
    (contains? number-formats pattern) (get number-formats pattern)
    (number? pattern) (do (assert (some (fn [[k v]] (== v pattern))
                                        number-formats)
                                  (str "Invalid number format: " pattern))
                          pattern)
    :else (name pattern)))


(defn formatter [pattern]
  (goog.i18n.DateTimeFormat. (resolve-pattern pattern)))

(defn format [date pattern & [tz]]
  "Format a given date, based on the current locale."
  (let [f (formatter pattern)]
    (.format f date tz)))


(defn parser [pattern]
  (goog.i18n.DateTimeParse. (resolve-pattern pattern)))

(defn parse [text pattern & {:keys [offset strict]}]
  "Parse a given string, using a specified pattern."
  (let [p (parser pattern)
        date (goog.date.DateTime.)]
    (if strict
      (.parse p text date offset)
      (.strictParse p text date offset))
    date))
