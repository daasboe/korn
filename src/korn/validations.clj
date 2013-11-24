(ns korn.validations
  (:require [jkkramer.verily :as v]))

;;;;;;;;;;;;
;; Recipe 
;;

(def recipe-name-validator
  (v/required :name))

(defn validate-recipe-name [m]
  (recipe-name-validator m))

(def efficiency-validator
  (v/combine
    (v/required :efficiency)
    (v/within 0 100 :efficiency)))

(defn validate-recipe-efficiency [m]
  (efficiency-validator m))

(def batch-size-validator
  (v/combine
    (v/required :batch_size)
    (v/within 0 999 :batch_size)))

(defn validate-recipe-batch-size [m]
  (batch-size-validator m))

(defn validate-recipe [m]
  ((v/combine
    validate-recipe-name
    validate-recipe-efficiency
    validate-recipe-batch-size) m))
