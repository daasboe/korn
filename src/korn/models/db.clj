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


(defn- insert-fixture [t k]
  (insert t (values (k (parse-string (slurp "./resources/fixtures.yml"))))))

(defn insert-fixtures []
  (insert-fixture users :users)
  (insert-fixture recipes :recipes)
  (insert-fixture hops :hops)
  (insert-fixture recipe_hops :recipe_hops))

(defn get-recipes []
  (select recipes))
          
(defn get-recipe [id]
  (select recipes
          (where {:id id})))

(defn delete-recipe [id]
  (delete recipes
          (where {:id id})))

(defn update-recipe [r]
  (update recipes
          (set-fields r)
          (where {:id (r :id)})))
