(ns lobos.config
  (:use lobos.connectivity)
  (:require [korn.models.schema :as schema]
            [lobos.migration :as lm]))

;This fixes where lobos looks for migrations
;(alter-var-root #'lobos.migration/*src-directory* (constantly "src/clj"))

(open-global schema/db-spec)
