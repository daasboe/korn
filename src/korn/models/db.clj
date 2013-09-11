(ns korn.models.db
  (:use korma.core
        [korma.db :only (defdb)]
        clj-yaml.core)
  (:require [korn.models.schema :as schema]))

(defdb db schema/db-spec)

(declare users hops recipes recipe_hops)

(defentity users
  (has-many recipes))

(defentity recipes
  (belongs-to users)
  (many-to-many hops :recipe_hops)
  (has-many recipe_hops))

(defentity hops
  (many-to-many recipes :recipe_hops)
  (has-many recipe_hops))

(defentity recipe_hops
  (belongs-to recipes)
  (belongs-to hops))

(defn save-recipe [{:keys [name type] :as recipe}]
  (insert recipes (values recipe)))


(defn- insert-fixture [t f]
  (insert t (values (f (parse-string (slurp "./resources/fixtures.yml"))))))

(defn insert-fixtures []
  (doseq [t '(users recipes hops recipe_hops)] (insert-fixture t (keyword t))))
