(ns loci)

(def ^:const locale
  "Locale can **only** be overriden via '--define goog.LOCALE=...',
   see note in `goog/base.js:59."
  goog.LOCALE)
