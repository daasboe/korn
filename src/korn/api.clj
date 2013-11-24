(ns korn.api
  (:use [hiccup.core :only (html)]
        [ korma.core])
  (:require [korn.routes :as r]
            [korn.models.db :as db]
            [shoreleave.middleware.rpc :refer [defremote]]))

(defremote testapi []
  (db/get-recipes))
  
(defremote get-recipes []
  (db/get-recipes))
  
(defremote get-recipe [id]
  (db/get-recipe id))

(defremote save-recipe [v]
  (db/save-recipe v))
  
(defremote delete-recipe [id]
  (db/delete-recipe id))
  
(defremote update-recipe [r]
  (db/update-recipe r))
  
