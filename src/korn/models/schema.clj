(ns korn.models.schema
  (:use [lobos.core :only (defcommand migrate)])
  (:require [noir.io :as io]
            [lobos.migration :as lm]))

(def db-spec
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :user "korn"
   :password "korn"
   :subname "//localhost:5432/korn_dev"})

(defcommand pending-migrations []
  (lm/pending-migrations db-spec sname))

(defn actualized? []
  (empty? (pending-migrations)))

(def actualize migrate)
