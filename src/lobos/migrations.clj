(ns lobos.migrations
  (:refer-clojure :exclude [alter drop bigint boolean char double float time])
  (:use (lobos [migration :only [defmigration]] core schema config helpers)))

(defmigration add-users-table
  (up [] (create
           (tbl :users
              (varchar :username 255 :unique)
              (varchar :password 255)
              (varchar :roles 255))))
  (down [] (drop (table :users))))

(defmigration add-recipes-table
  (up [] (create
           (tbl :recipes
              (varchar :name 255)
              (varchar :type 255)
              (integer :batch_size)
              (integer :boil_size)
              (integer :boil_time)
              (integer :efficiency)
              (refer-to :users))))
  (down [] (drop (table :recipes))))

(defmigration add-hops-table
  (up [] (create
           (tbl :hops
              (varchar :name 255)
              (integer :alpha)
              (text    :notes)
              (varchar :type 255)
              (varchar :form 255))))
  (down [] (drop (table :hops))))

(defmigration add-recipe-hops-table
  (up [] (create
           (tbl :recipe_hops
              (refer-to :recipes)
              (refer-to :hops)
              (varchar  :use 255)
              (integer  :time))))
  (down [] (drop (table :recipe_hops))))
